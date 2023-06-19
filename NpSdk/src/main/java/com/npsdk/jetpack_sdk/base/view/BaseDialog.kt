package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.R
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.viewmodel.AppViewModel
import kotlinx.coroutines.*

@Composable
fun BaseDialog(content: @Composable () -> Unit, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = {}, content = content
    )
}

@Composable
fun DialogNotification(contextString: String, onDismiss: () -> Unit, titleButon: String? = "Quay lại") {
    Dialog(onDismissRequest = {

    }) {
        Column(
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(color = Color.White).padding(12.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thông báo", color = Color.Black, fontSize = 14.sp, fontFamily = fontAppBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = contextString,
                color = colorResource(R.color.titleText),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontFamily = fontAppDefault
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).fillMaxWidth().height(36.dp).background(
                    colorResource(R.color.green)
                ).clickable {
                    onDismiss()
                },
            ) {
                Text(titleButon!!, fontFamily = fontAppDefault, fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun LoadingView() {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    var delayJob: Job? = null
    val appViewModel: AppViewModel = viewModel()
    if (appViewModel.isShowLoading) LaunchedEffect(true) {
        delayJob?.cancel()
        delayJob = coroutineScope.launch {
            delay(30000)
            appViewModel.hideLoading()
        }
    }
    CircularProgressIndicator(color = colorResource(R.color.green))

//    val imageLoader = ImageLoader.Builder(LocalContext.current).components {
//        if (SDK_INT >= 28) {
//            add(ImageDecoderDecoder.Factory())
//        } else {
//            add(GifDecoder.Factory())
//        }
//    }.build()
//
//    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
//        Image(
//            painter = rememberAsyncImagePainter(R.drawable.loading, imageLoader),
//            contentDescription = null,
//            modifier = Modifier.clip(RoundedCornerShape(8.dp)).size(150.dp).background(Color.Transparent)
//        )
//    }
}

@Composable
fun ShowBackDialog(onBack: () -> Unit = {}, onContinue: () -> Unit = {}) {
    Dialog(onDismissRequest = {
        onContinue()
    }) {
        Column(
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(color = Color.White).padding(12.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.Text(
                text = "Bạn có chắc chắn muốn quay lại?",
                color = colorResource(R.color.black),
                fontSize = 14.sp,
                fontFamily = fontAppBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            androidx.compose.material3.Text(
                text = "Giao dịch thanh toán của bạn sẽ bị hủy nếu bạn thực hiện quay lại.",
                color = colorResource(R.color.titleText),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                fontFamily = fontAppDefault
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).fillMaxWidth().height(36.dp)
                        .background(
                            colorResource(R.color.background)
                        ).clickableWithoutRipple{
                            onBack()
                        }
                ) {
                    androidx.compose.material3.Text(
                        "Quay lại",
                        textAlign = TextAlign.Center,
                        fontFamily = fontAppBold,
                        fontSize = 12.sp,
                        color = colorResource(R.color.green)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).fillMaxWidth().height(36.dp)
                        .background(
                            colorResource(R.color.green)
                        ).clickableWithoutRipple{
                            onContinue()
                        }
                ) {
                    androidx.compose.material3.Text(
                        "Không",
                        textAlign = TextAlign.Center,
                        fontFamily = fontAppBold,
                        fontSize = 12.sp,
                        color = colorResource(R.color.white)
                    )
                }
            }

        }
    }
}