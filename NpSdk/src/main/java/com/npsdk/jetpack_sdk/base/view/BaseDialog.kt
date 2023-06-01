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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import kotlinx.coroutines.*
import com.npsdk.module.R
import com.npsdk.jetpack_sdk.viewmodel.AppViewModel

@Composable
fun BaseDialog(content: @Composable () -> Unit, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = {}, content = content
    )
}

@Composable
fun DialogNotification(contextString: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {

    }) {
        Column(
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(color = Color.White).padding(12.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thông báo", color = Color.Black, fontSize = 18.sp, fontFamily = fontAppBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = contextString,
                color = colorResource(R.color.titleText),
                fontSize = 12.sp,
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
                Text("Quay lại", fontFamily = fontAppDefault, fontSize = 12.sp, color = Color.White)
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