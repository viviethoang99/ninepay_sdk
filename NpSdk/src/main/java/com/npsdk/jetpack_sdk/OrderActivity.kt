package com.npsdk.jetpack_sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.Utils
import com.npsdk.jetpack_sdk.base.view.*
import com.npsdk.jetpack_sdk.repository.CallbackOrder
import com.npsdk.jetpack_sdk.repository.CheckValidatePayment
import com.npsdk.jetpack_sdk.repository.model.ListBankModel
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel
import com.npsdk.jetpack_sdk.repository.model.validate_payment.Methods
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel

class DataOrder {
    companion object {
        var urlData: String = ""
        var dataOrderSaved: ValidatePaymentModel? = null
        var amount: Any? = null
        var listBankModel: ListBankModel? = null
        var activityOrder: Activity? = null

        var selectedItemDefault by mutableStateOf<Methods?>(null)
        var selectedItemMethod by mutableStateOf<Methods?>(null)
        var balance by mutableStateOf<Int?>(null)

        fun isWallet(): Boolean {
            return selectedItemMethod?.code == "WALLET"
        }

        fun isInland(): Boolean {
            return selectedItemMethod?.code == "ATM_CARD"
        }

        fun isInternational(): Boolean {
            return selectedItemMethod?.code == "CREDIT_CARD"
        }
    }
}


class OrderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataOrder.activityOrder = this
        val bundle = intent.extras
        if (bundle != null) {
            DataOrder.urlData = bundle.getString("url") ?: ""
        }
        setContent {
            PaymentNinepayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.background)
                ) {
                    Body()
                }
            }
        }

    }
}


@Composable
private fun Body() {
    var modalOrderData by remember { mutableStateOf<ValidatePaymentModel?>(null) }
    val inputViewModel: InputViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(true) {
        if (DataOrder.dataOrderSaved != null) {
            DataOrder.amount =
                (DataOrder.dataOrderSaved!!.data.listPaymentData.find { it.name.equals("Giá trị đơn hàng") }?.value)
            DataOrder.selectedItemMethod = DataOrder.selectedItemDefault
            modalOrderData = DataOrder.dataOrderSaved
            return@LaunchedEffect
        }
        CheckValidatePayment().check(context, DataOrder.urlData, CallbackOrder { data ->
            modalOrderData = data
            DataOrder.dataOrderSaved = data
            DataOrder.amount =
                (DataOrder.dataOrderSaved!!.data.listPaymentData.find { it.name.equals("Giá trị đơn hàng") }?.value)
        })

    }

    if (modalOrderData == null) {
        ShimmerLoading()
        return
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
        LazyColumn(verticalArrangement = Arrangement.Top) {
            item { TopAppBarApp(isShowBack = false) }
            item {
                Box(modifier = Modifier.padding(12.dp)) {
                    HeaderOrder(modalOrderData!!)
                }
            }
            item {
                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                    ShowMethodPayment(modalOrderData!!, onItemClick = { itemCallback ->
                        DataOrder.selectedItemMethod = itemCallback
                    })
                }
            }

            item {
                if (inputViewModel.showNotification.value) {
                    DialogNotification(contextString = inputViewModel.stringDialog.value, onDismiss = {
                        inputViewModel.showNotification.value = false
                    })
                }
            }
        }
        Footer(modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp),

            clickContinue = { ->
                // context.startActivity(Intent(context, PasswordActivity::class.java))
                if (DataOrder.isWallet()) {
//                    createOrderWallet(inputViewModel, context, appViewModel)
                    context.startActivity(Intent(context, PasswordActivity::class.java))
                    return@Footer
                }
                context.startActivity(Intent(context, InputCardActivity::class.java))
            })
    }


}


@Composable
private fun Footer(modifier: Modifier, clickContinue: () -> Unit) {
    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).fillMaxWidth().height(44.dp).background(
                if (DataOrder.selectedItemMethod == null) colorResource(R.color.grey) else colorResource(R.color.green)
            ).clickable {
                if (DataOrder.selectedItemMethod != null) clickContinue()
            },
        ) {
            androidx.compose.material.Text(
                "Tiếp tục", fontFamily = fontAppDefault, fontSize = 12.sp, color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PaymentNinepayTheme {
        Body()
    }
}

@Composable
private fun ShowMethodPayment(data: ValidatePaymentModel, onItemClick: (Methods) -> Unit) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        if (DataOrder.selectedItemDefault != null) Text(
            text = "Phương thức thanh toán", style = TextStyle(
                fontWeight = FontWeight.W600, fontSize = 12.sp, fontFamily = fontAppBold
            )
        ) else Text(
            text = "Chọn phương thức thanh toán", style = TextStyle(
                fontWeight = FontWeight.W600, fontSize = 12.sp, fontFamily = fontAppBold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.clip(shape = RoundedCornerShape(12.dp)).background(Color.White).padding(vertical = 8.dp)
        ) {
            if (DataOrder.selectedItemDefault?.code == null) data.data.methods.forEachIndexed { index, item ->

                Column {
                    ItemRow(item, item.code == DataOrder.selectedItemMethod?.code) { ->
                        onItemClick(item)
                    }
                    if (index != data.data.methods.size - 1) Divider(
                        modifier = Modifier.padding(start = 50.dp),
                        color = Color(0XFFF1F3F4)
                    )
                }
            } else Column {
                DataOrder.selectedItemDefault?.let { ItemRow(it, true) {} }
            }
        }
    }
}

@Composable
private fun ItemRow(item: Methods, isChecked: Boolean, onItemClick: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickableWithoutRipple {
            onItemClick()
        }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        ImageFromUrl(
            item.icon, modifier = Modifier.width(36.dp).height(36.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            val parseAmount: Int =
                if (DataOrder.amount is Double) (DataOrder.amount as Double).toInt() else (DataOrder.amount as Int)
            Column {
                Text(
                    item.name,
                    style = TextStyle(fontWeight = FontWeight.W600, fontSize = 13.sp, fontFamily = fontAppDefault)
                )
                DataOrder.balance?.let {
                    if (it < parseAmount) Text(
                        "${Utils.formatMoney(it)} - Không đủ",
                        style = TextStyle(
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp,
                            fontFamily = fontAppDefault,
                            color = colorResource(R.color.yellow)
                        )
                    )
                }
            }

            if (DataOrder.balance != null && DataOrder.balance!! < parseAmount) Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(80.dp).height(28.dp).clip(RoundedCornerShape(14.dp))
                    .background(colorResource(R.color.background))
            ) {
                Text(
                    "Nạp tiền",
                    style = TextStyle(
                        color = colorResource(R.color.green),
                        fontSize = 12.sp,
                        fontFamily = fontAppDefault,
                        fontWeight = FontWeight.W600
                    )
                )
            } else
                Image(
                    painter = painterResource(if (isChecked) R.drawable.radio_checked else R.drawable.radio_no_check),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
        }
    }
}