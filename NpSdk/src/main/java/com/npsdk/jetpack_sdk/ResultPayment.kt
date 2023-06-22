package com.npsdk.jetpack_sdk

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.Utils
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class ResultPayment : ComponentActivity() {

    var activity: Activity? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }

        OnBackPressedDispatcher().addCallback(this, callback)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {

            }
        }

        setContent {
            PaymentNinepayTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Scaffold(
                        content = { paddingValues ->
                            Body(paddingValues = paddingValues)
                        })
                }
            }
        }
    }

    @Composable
    private fun Body(paddingValues: PaddingValues?) {
        var ticks by remember { mutableStateOf(7) }

        LaunchedEffect(true) {
            while (ticks > 0) {
                delay(1.seconds)
                ticks--
            }
            if (ticks == 0) {
                activity?.finish()
            }
        }

        Box(
            modifier = Modifier.padding(top = paddingValues!!.calculateTopPadding()).fillMaxSize()
                .background(colorResource(id = R.color.background)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.fillMaxWidth().fillMaxSize()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    Box(
                        modifier = Modifier.height(100.dp).background(color = colorResource(R.color.green))
                            .fillMaxWidth()
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "Hoá đơn giao dịch",
                            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                            textAlign = TextAlign.Center, style = TextStyle(
                                colorResource(R.color.white),
                                fontWeight = FontWeight.W600,
                                fontFamily = fontAppDefault, fontSize = 14.sp
                            )
                        )

                        DataOrder.dataOrderSaved?.let {
                            var nameMerchant: String = it.data.merchantInfo.name
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .clip(shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                    .background(Color.White).padding(horizontal = 16.dp).padding(top = 16.dp)
                            ) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        modifier = Modifier.size(52.dp),
                                        painter = painterResource(R.drawable.success),
                                        contentDescription = null
                                    )
                                    Text(
                                        modifier = Modifier.padding(vertical = 18.dp),
                                        text = "Thanh toán cho $nameMerchant",
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontWeight = FontWeight.W400, color = colorResource(
                                                id = R.color.titleText
                                            ), fontSize = 12.sp, fontFamily = fontAppDefault
                                        )
                                    )
                                    DataOrder.feeTemp?.let {
                                        Text(
                                            text = Utils.formatMoney(it),
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                fontWeight = FontWeight.W600, fontSize = 18.sp, fontFamily = fontAppBold
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Box(
                                        Modifier.height(1.dp).fillMaxWidth()
                                            .background(Color.Gray, shape = DottedShape(step = 5.dp))
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    it.data.listPaymentData.map { rowItem ->
                                        Row(
                                            modifier = Modifier.padding(bottom = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                rowItem.name,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Start,
                                                style = TextStyle(
                                                    color = colorResource(id = R.color.titleText),
                                                    fontWeight = FontWeight.W400,
                                                    fontSize = 12.sp,
                                                    fontFamily = fontAppDefault
                                                )
                                            )

                                            Text(
                                                text = if (rowItem.value is Double || rowItem.value is Int) Utils.formatMoney(
                                                    rowItem.value
                                                ) else rowItem.value.toString(),
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.End,
                                                style = TextStyle(fontFamily = fontAppBold, fontSize = 12.sp)

                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Divider(color = colorResource(R.color.divider))
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            "Giao dịch thực hiện bởi",
                                            style = TextStyle(
                                                colorResource(R.color.grey),
                                                fontFamily = fontAppDefault,
                                                fontSize = 10.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Image(
                                            modifier = Modifier.width(40.dp).height(24.dp),
                                            painter = painterResource(R.drawable.logo_9pay),
                                            contentDescription = null
                                        )
                                    }

                                }
                            }
                        }

                        Image(
                            modifier = Modifier.fillMaxWidth().height(12.dp),
                            painter = painterResource(R.drawable.subtract),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            "Bạn sẽ được chuyển về trang gốc sau ${ticks}s ...",
                            style = TextStyle(
                                fontFamily = fontAppDefault,
                                color = colorResource(R.color.titleText),
                                fontSize = 12.sp
                            )
                        )
                    }
                }

            }


        }
    }

    override fun onBackPressed() {
        return
    }
}
