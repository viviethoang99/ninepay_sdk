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
import com.npsdk.jetpack_sdk.repository.model.ListBankModel
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel
import com.npsdk.jetpack_sdk.repository.CallbackListBank
import com.npsdk.jetpack_sdk.repository.CallbackOrder
import com.npsdk.jetpack_sdk.repository.CheckValidatePayment
import com.npsdk.jetpack_sdk.repository.GetListBank
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel
import com.npsdk.jetpack_sdk.repository.model.validate_payment.Methods
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.viewmodel.AppViewModel
import com.npsdk.jetpack_sdk.viewmodel.OrderViewModel
import com.npsdk.module.R
import com.npsdk.jetpack_sdk.base.view.*
import com.npsdk.jetpack_sdk.theme.fontAppBold

var urlData: String = ""
var dataOrderSaved: ValidatePaymentModel? = null
var methodsSelected: Methods? = null
var listBankModel: ListBankModel? = null
var activityOrder: Activity? = null

fun isWallet(): Boolean = methodsSelected?.code == "WALLET"
fun isInland(): Boolean = methodsSelected?.code == "ATM_CARD"
fun isInternational(): Boolean = methodsSelected?.code == "CREDIT_CARD"

class OrderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOrder = this
        val bundle = intent.extras
        if (bundle != null) {
            urlData = bundle.getString("url") ?: ""
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
    val appViewModel: AppViewModel = viewModel()
    val inputViewModel: InputViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(true) {
        CheckValidatePayment().check(context, urlData, CallbackOrder { data ->
            modalOrderData = data
            dataOrderSaved = data
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
                        methodsSelected = itemCallback
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
        if (appViewModel.isShowLoading) Box(Modifier.align(Alignment.Center)) {
            LoadingView()
        }
        Footer(modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp),

            clickContinue = { ->
                if (isWallet()) {
                    createOrderWallet(inputViewModel, context, appViewModel)
                    return@Footer
                }
                if (listBankModel != null) {
                    appViewModel.hideLoading()
                    context.startActivity(Intent(context, InputCardActivity::class.java))
                    return@Footer
                }
                appViewModel.showLoading()
                GetListBank().get(context, CallbackListBank { response ->
                    appViewModel.hideLoading()
                    listBankModel = response
                    context.startActivity(Intent(context, InputCardActivity::class.java))
                })
            })
    }


}


@Composable
private fun Footer(modifier: Modifier, clickContinue: () -> Unit) {
    val viewModel: OrderViewModel = viewModel()
    Column(
        modifier = modifier
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).fillMaxWidth().height(44.dp).background(
                if (viewModel.selectedItemId.value == null) colorResource(R.color.grey) else colorResource(R.color.green)
            ).clickable {
                if (viewModel.selectedItemId.value != null) clickContinue()
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
    val viewModel: OrderViewModel = viewModel()
    viewModel.listMethod = data.data.methods
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Chọn phương thức thanh toán", style = TextStyle(
                fontWeight = FontWeight.W600, fontSize = 12.sp, fontFamily = fontAppBold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.clip(shape = RoundedCornerShape(12.dp)).background(Color.White).padding(vertical = 8.dp)
        ) {
            viewModel.listMethod.forEachIndexed { index, item ->

                Column {
                    ItemRow(item, viewModel.listMethod[index].name == viewModel.selectedItemId.value) { ->
                        viewModel.selectedItemId.value = item.name
                        onItemClick(item)
                    }
                    Divider(modifier = Modifier.padding(start = 50.dp), color = Color(0XFFF1F3F4))
                }
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

            Text(item.name, style = TextStyle(fontWeight = FontWeight.W600, fontSize = 13.sp))

            Image(
                painter = painterResource(if (isChecked) R.drawable.radio_checked else R.drawable.radio_no_check),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}