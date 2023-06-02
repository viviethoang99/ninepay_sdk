package com.npsdk.jetpack_sdk

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.R
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class ErrorPaymentActivity : ComponentActivity() {

    private var message : String = ""
    private val keyBundle = "message"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(keyBundle)) {
                message = bundle.getString(keyBundle).toString()
            }
        }
        setContent {
            PaymentNinepayTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Body(message)
                }
            }
        }
    }
}

@Composable
private fun Body(message: String) {
    val context = LocalContext.current
    var ticks by remember { mutableStateOf(5) }

    LaunchedEffect(true) {
        while (ticks > 0) {
            delay(1.seconds)
            ticks--
        }
        if (ticks == 0) {
            (context as Activity).finish()
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = 40.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(R.drawable.icon_error),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = fontAppDefault,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                color = colorResource(R.color.titleText)
            )
        )
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = "Bạn sẽ được chuyển về trang chủ sau $ticks giây...",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = fontAppDefault,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                color = colorResource(R.color.titleText)
            )
        )
    }
}