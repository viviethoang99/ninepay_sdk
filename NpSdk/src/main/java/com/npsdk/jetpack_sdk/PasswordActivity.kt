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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.view.TopAppBarApp
import com.npsdk.jetpack_sdk.base.view.clickableWithoutRipple
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault

class PasswordActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        Box(
            modifier = Modifier.padding(top = paddingValues!!.calculateTopPadding()).fillMaxSize()
                .background(colorResource(id = R.color.background))
        ) {
            LazyColumn {
                item {
                    Box(modifier = Modifier.padding(12.dp)) {
                        FieldPassword()
                    }
                }
            }

            Footer(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 12.dp),
                clickContinue = {})
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun FieldPassword() {
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

                Box (
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
                            }
                        })
                    LineDot(passwordStr.length)
                }


                Text("Mật khẩu không chính xác, bạn còn 2 lần thử", style = TextStyle(
                    color = colorResource(R.color.red),
                    fontSize = 11.sp,
                    fontFamily = fontAppDefault
                ))

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Quên mật khẩu?",
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