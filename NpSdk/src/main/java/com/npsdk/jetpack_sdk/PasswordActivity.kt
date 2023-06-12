package com.npsdk.jetpack_sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.view.DialogNotification
import com.npsdk.jetpack_sdk.base.view.LoadingView
import com.npsdk.jetpack_sdk.base.view.TopAppBarApp
import com.npsdk.jetpack_sdk.base.view.clickableWithoutRipple
import com.npsdk.jetpack_sdk.repository.CallbackVerifyPayment
import com.npsdk.jetpack_sdk.repository.VerifyPayment
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.viewmodel.AppViewModel
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel
import com.npsdk.module.NPayLibrary
import com.npsdk.module.utils.Actions

class PasswordActivity : ComponentActivity() {

    private var paymentId: String? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            paymentId = bundle.getString("paymentId")
        }
        setContent {
            PaymentNinepayTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        TopAppBarApp()
                    }, content = { paddingValues ->
                        Body(paddingValues = paddingValues)
                    })
                }
            }
        }
    }

    @Composable
    private fun Body(paddingValues: PaddingValues?) {
        val context = LocalContext.current
        val appViewModel: AppViewModel = viewModel()
        val inputViewModel: InputViewModel = viewModel()
        var passwordSaved = ""
        var errTextPassword by remember {
            mutableStateOf("")
        }

        Box(
            modifier = Modifier.padding(top = paddingValues!!.calculateTopPadding()).fillMaxSize()
                .background(colorResource(id = R.color.background))
        ) {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Box(modifier = Modifier.padding(12.dp)) {
                        FieldPassword(onTextChanged = {
                            passwordSaved = it
                            if (passwordSaved.length == 6) {
                                errTextPassword = ""
                                // Verify password
                                appViewModel.showLoading()
                                VerifyPayment().create(
                                    context,
                                    paymentId,
                                    passwordSaved,
                                    CallbackVerifyPayment { message: String? ->
                                        appViewModel.hideLoading()
                                        if (message != null) {
                                            errTextPassword = message
                                        } else {
                                            // Done
                                            NPayLibrary.getInstance().listener.onPaySuccessful()
                                        }
                                    })
                            }
                        }, errTextPassword)
                    }
                }
                item {
                    if (inputViewModel.showNotification.value) {
                        DialogNotification(contextString = inputViewModel.stringDialog.value, onDismiss = {
                            inputViewModel.showNotification.value = false
                        })
                    }
                }
                item {
                    if (appViewModel.isShowLoading) LoadingView()
                }
            }

            Footer(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 12.dp),
                clickContinue = {
                    if (passwordSaved.length < 6) {
                        errTextPassword = "Vui lòng nhập mật khẩu để tiếp tục!"
                        return@Footer
                    }
                    // Verify password
                    appViewModel.showLoading()
                    VerifyPayment().create(
                        context,
                        paymentId,
                        passwordSaved,
                        CallbackVerifyPayment { message: String? ->
                            appViewModel.hideLoading()
                            if (message != null) {
                                errTextPassword = message
                            } else {
                                // Done
                                NPayLibrary.getInstance().listener.onPaySuccessful()
                            }
                        })
                })
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun FieldPassword(onTextChanged: (String) -> Unit = {}, errTextPassword: String) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        var passwordStr by remember {
            mutableStateOf("")
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(
                colorResource(R.color.white)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 30.dp)
            ) {
                Text(
                    "Vui lòng nhập mật khẩu để xác nhận giao dịch",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = fontAppBold
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickableWithoutRipple {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }) {
                    BasicTextField(
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                            .background(colorResource(R.color.white)).focusRequester(focusRequester),
                        maxLines = 1,
                        minLines = 1,
                        singleLine = true,
                        cursorBrush = SolidColor(Color.White),
                        textStyle = TextStyle(colorResource(R.color.white)),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        value = passwordStr,
                        onValueChange = {
                            if (it.length <= 6) {
                                passwordStr = it
                                onTextChanged(passwordStr)
                            }
                        })
                    LineDot(passwordStr.length)
                }


                Text(
                    errTextPassword, style = TextStyle(
                        color = colorResource(R.color.red),
                        fontSize = 11.sp,
                        fontFamily = fontAppDefault
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.clickableWithoutRipple {
                        // Gọi sang webview login
                        NPayLibrary.getInstance().openWallet(Actions.LOGIN)
                    },
                    text = "Quên mật khẩu?",
                    style = TextStyle(
                        color = colorResource(R.color.green),
                        fontSize = 13.sp,
                        fontFamily = fontAppBold
                    )
                )

            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    @Composable
    private fun LineDot(lengthPin: Int) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(6) { index ->
                Box(
                    modifier = Modifier.size(16.dp).background(
                        colorResource(if (lengthPin <= index) R.color.grey else R.color.green),
                        shape = CircleShape
                    ),
                ) {
                }
            }

        }
    }

    @Composable
    private fun Footer(
        modifier: Modifier,
        clickContinue: () -> Unit
    ) {
        Column(
            modifier = modifier
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).fillMaxWidth().height(44.dp).background(
                    colorResource(R.color.green)
                ).clickable {
                    clickContinue()
                },
            ) {
                Text(
                    "Tiếp tục", fontFamily = fontAppDefault, fontSize = 12.sp, color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}