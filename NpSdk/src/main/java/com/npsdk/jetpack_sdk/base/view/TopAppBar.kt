package com.npsdk.jetpack_sdk.base.view

import android.app.Activity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.jetpack_sdk.theme.fontAppBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarApp(title: String = "Thanh toán", isShowBack: Boolean? = true, onBack: () -> Unit = {}) {
    var context = LocalContext.current
    Row(modifier = Modifier.height(50.dp).fillMaxWidth()) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(), title = {
                Text(
                    text = title,
                    style = TextStyle(fontFamily = fontAppBold, textAlign = TextAlign.Center, fontSize = 14.sp)
                )
            }, navigationIcon = {
                if (isShowBack!!) IconButton(onClick = {
                    (context as Activity).onBackPressed()
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                    )
                }
            }

        )
    }
}