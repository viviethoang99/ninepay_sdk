package com.npsdk.jetpack_sdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.listener.CloseListener
import com.npsdk.jetpack_sdk.base.view.*
import com.npsdk.jetpack_sdk.repository.*
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsWallet
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.theme.initColor
import com.npsdk.jetpack_sdk.viewmodel.AppViewModel
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel
import com.npsdk.module.NPayLibrary
import com.npsdk.module.PaymentMethod
import com.npsdk.module.utils.*

class PasswordActivity : ComponentActivity() {


    var showDialog by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataOrder.isProgressing = false
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog = true
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                showDialog = true
            }
        }
        setContent {
            PaymentNinepayTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        TopAppBarApp(
                            onBack = {
                                showDialog = true
                            }
                        )
                    }, content = { paddingValues ->
                        Body(paddingValues = paddingValues)
                    })
                }
            }
        }
        CloseListener().listener(this)
    }

    override fun onDestroy() {
        CloseListener().cancelListener(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        showDialog = true
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
                        FieldPassword(onTextChanged = { password ->
                            passwordSaved = password
                            if (passwordSaved.length == 6) {
                                errTextPassword = ""
                                // Verify password and create order, payment.
                                appViewModel.showLoading()
                                VerifyPassword().check(context, passwordSaved, CallbackVerifyPassword {
                                    // Message khac null tuc la co loi xay ra, show loi ra man hinh
                                    if (it != null) {
                                        appViewModel.hideLoading()
                                        errTextPassword = it
                                        return@CallbackVerifyPassword
                                    } else {
                                        // Khong co loi gi, tao order
                                        createOrderWallet(context, inputViewModel, appViewModel, passwordSaved,
                                            messageError = { callback ->
                                                inputViewModel.showNotification.value = true
                                                inputViewModel.stringDialog.value = callback
                                            })
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
                    if (appViewModel.isShowLoading) {
                        return@Footer
                    }
                    if (passwordSaved.length < 6) {
                        errTextPassword = "Vui lòng nhập mật khẩu để tiếp tục!"
                        return@Footer
                    }
                    // Verify password and create order, payment.
                    appViewModel.showLoading()
                    VerifyPassword().check(context, passwordSaved, CallbackVerifyPassword {
                        // Message khac null tuc la co loi xay ra, show loi ra man hinh
                        if (it != null) {
                            appViewModel.hideLoading()
                            errTextPassword = it
                            return@CallbackVerifyPassword
                        } else {
                            // Khong co loi gi, tao order
                            createOrderWallet(context, inputViewModel, appViewModel, passwordSaved,
                                messageError = { callback ->
                                    inputViewModel.showNotification.value = true
                                    inputViewModel.stringDialog.value = callback
                                })
                        }
                    })

                })

            if (showDialog) ShowConfirmDialog(onLeft = {
                showDialog = false
                NPayLibrary.getInstance().callbackBackToAppfrom(NameCallback.PAYMENT_SCREEN)

                finish()
            }, onRight = {
                showDialog = false
            }, onDismiss = {
                showDialog = false
            }
            )
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun FieldPassword(onTextChanged: (String) -> Unit = {}, errTextPassword: String) {
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        var passwordStr by remember {
            mutableStateOf("")
        }

        LaunchedEffect(errTextPassword) {
            if (errTextPassword.isNotBlank()) {
                if (errTextPassword != "Vui lòng nhập mật khẩu để tiếp tục!") {
                    passwordStr = ""
                }
            }
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
                    // Hide copy, cut, paste
                    CompositionLocalProvider(LocalTextToolbar provides EmptyTextToolbar) {
                        // PIN Field Password
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
                    }
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
                        DataOrder.isProgressing = true
                        // Gọi sang webview login
                        val phone = Preference.getString(context, Flavor.prefKey + Constants.PHONE, "")
                        NPayLibrary.getInstance().openSDKWithAction(Actions.forgotPassword(phone))
                    },
                    text = "Quên mật khẩu?",
                    style = TextStyle(
                        color = initColor(),
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
                        if (lengthPin <= index) colorResource(R.color.grey) else initColor(),
                        shape = CircleShape
                    ),
                ) {
                }
            }

        }
    }

    object EmptyTextToolbar : TextToolbar {
        // Hide copy, cut, paste
        override val status: TextToolbarStatus = TextToolbarStatus.Hidden
        override fun hide() {}
        override fun showMenu(
            rect: Rect,
            onCopyRequested: (() -> Unit)?,
            onPasteRequested: (() -> Unit)?,
            onCutRequested: (() -> Unit)?,
            onSelectAllRequested: (() -> Unit)?,
        ) {
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
                    initColor()
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

    private fun createOrderWallet(
        context: Context,
        inputView: InputViewModel,
        appViewModel: AppViewModel,
        password: String,
        messageError: (String) -> Unit
    ) {
        appViewModel.showLoading()
        val params = CreateOrderParamsWallet(
            url = DataOrder.urlData, method = PaymentMethod.WALLET,
            amount = DataOrder.totalAmount.toString()
        )
        CreateOrderWalletRepo().create(context, params, CallbackCreateOrder {

            it.message?.let { it1 ->
                if (it.errorCode == 1) {
                    NPayLibrary.getInstance().callBackToMerchant(
                        NameCallback.SDK_PAYMENT, false, null
                    )
                    appViewModel.hideLoading()
                    inputView.showNotification.value = true
                    inputView.stringDialog.value = it1
                } else if (it.errorCode == 0) {
                    val orderId = Utils.convertUrlToOrderId(it.data!!.redirectUrl!!)
                    CreatePayment().create(context, orderId, CallbackCreatePayment { paymentId, message ->
                        run {
                            if (paymentId == null) {
                                NPayLibrary.getInstance().callBackToMerchant(
                                    NameCallback.SDK_PAYMENT, false, null
                                )
                                appViewModel.hideLoading()
                                inputView.showNotification.value = true
                                inputView.stringDialog.value = message
                                return@run
                            } else {
                                // Tạo payment ví.
                                VerifyPayment().create(
                                    context,
                                    paymentId,
                                    password,
                                    CallbackVerifyPayment { message: String? ->
                                        appViewModel.hideLoading()
                                        if (message != null) {
                                            NPayLibrary.getInstance().callBackToMerchant(
                                                NameCallback.SDK_PAYMENT, false, null
                                            )
                                            messageError(message)
                                        } else {
                                            (context as Activity).finish() // Close screen
                                            DataOrder.activityOrder?.finish()
                                            if (DataOrder.isShowResultScreen) {
                                                // Move to result screen
                                                val intent = Intent(context, ResultPayment::class.java)
                                                intent.putExtra("status", Constants.SUCCESS)
                                                context.startActivity(intent)
                                            } else {
                                                // Done
                                                NPayLibrary.getInstance().callBackToMerchant(
                                                    NameCallback.SDK_PAYMENT, true, null
                                                )
                                                NPayLibrary.getInstance().listener.onCloseSDK()
                                            }
                                        }
                                    })
                            }
                        }
                    })
                }
            }
        })
    }
}