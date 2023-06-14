package com.npsdk.jetpack_sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.Utils
import com.npsdk.jetpack_sdk.base.view.*
import com.npsdk.jetpack_sdk.repository.*
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsWallet
import com.npsdk.jetpack_sdk.repository.model.ListBankModel
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel
import com.npsdk.jetpack_sdk.repository.model.validate_payment.Methods
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.viewmodel.AppViewModel
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel
import com.npsdk.module.NPayLibrary
import com.npsdk.module.utils.Actions

class DataOrder {
    companion object {
        var urlData: String = ""
        var dataOrderSaved by mutableStateOf<ValidatePaymentModel?>(null)
        var amount: Any? = null
        var listBankModel: ListBankModel? = null

        @SuppressLint("StaticFieldLeak")
        var activityOrder: Activity? = null
        var selectedItemMethod by mutableStateOf<String?>(null)
        var balance by mutableStateOf<Int?>(null)

        var feeTemp by mutableStateOf<Int?>(null)
    }
}


class OrderActivity : ComponentActivity() {

    private var methodDefault: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataOrder.amount = null
        DataOrder.feeTemp = null
        DataOrder.dataOrderSaved = null
        DataOrder.selectedItemMethod = null

        DataOrder.activityOrder = this
        val bundle = intent.extras
        if (bundle != null) {
            methodDefault = bundle.getString("method")
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

    @Composable
    private fun Body() {
        var modalOrderData by remember { mutableStateOf<ValidatePaymentModel?>(null) }
        val inputViewModel: InputViewModel = viewModel()
        val appViewModel: AppViewModel = viewModel()
        val context = LocalContext.current

        LaunchedEffect(true) {

            // Get detail payment
            CheckValidatePayment().check(context, DataOrder.urlData, CallbackOrder { data ->
                modalOrderData = data
                DataOrder.dataOrderSaved = data
                DataOrder.amount =
                    (DataOrder.dataOrderSaved!!.data.feeData.wallet).toInt()
                setDefaultAmount()
            })

            // Get user info
            NPayLibrary.getInstance().getUserInfoSendToPayment()
        }

        if (modalOrderData == null) {
            ShimmerLoading()
            return
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            LazyColumn(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                item { TopAppBarApp(isShowBack = false) }
                item {
                    Box(modifier = Modifier.padding(12.dp)) {
                        HeaderOrder(modalOrderData!!)
                    }
                }
                item {
                    Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                        ShowMethodPayment(modalOrderData!!, onItemClick = { itemCallback ->
                            DataOrder.selectedItemMethod = itemCallback.code
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
                item {
                    if (appViewModel.isShowLoading) LoadingView()
                }
            }
            Footer(modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp),

                clickContinue = { ->
                    if (methodDefault == "WALLET" || DataOrder.selectedItemMethod == "WALLET") {
                        if (DataOrder.balance == null) {
                            inputViewModel.showNotification.value = true
                            inputViewModel.stringDialog.value = "Không thể lấy dữ liệu tài khoản ví!!!"
                            return@Footer
                        }
                        DataOrder.balance?.let { it1 ->
                            DataOrder.feeTemp?.let { it2 ->
                                if (it1 < it2) {
                                    inputViewModel.showNotification.value = true
                                    inputViewModel.stringDialog.value = "Số dư không đủ để thực hiện giao dịch, vui lòng nạp thêm tiền!"
                                    return@Footer
                                }
                            }
                        }

                        createOrderWallet(inputViewModel, context, appViewModel)
                        return@Footer
                    }
                    val intent = Intent(context, InputCardActivity::class.java)
                    intent.putExtra("method", DataOrder.selectedItemMethod)
                    context.startActivity(intent)
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


    @Composable
    private fun ShowMethodPayment(data: ValidatePaymentModel, onItemClick: (Methods) -> Unit) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))
            if (methodDefault == "WALLET") Text(
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
                modifier = Modifier.clip(shape = RoundedCornerShape(12.dp)).background(Color.White)
                    .padding(vertical = 8.dp)
            ) {
                if (methodDefault == null) data.data.methods.forEachIndexed { index, item ->

                    Column {
                        ItemRow(item, item.code == DataOrder.selectedItemMethod) { ->
                            onItemClick(item)
                        }
                        if (index != data.data.methods.size - 1) Divider(
                            modifier = Modifier.padding(start = 50.dp),
                            color = Color(0XFFF1F3F4)
                        )
                    }
                } else Column {
                    var methodFind: Methods? = null
                    data.data.methods.forEach { item ->
                        if (item.code == methodDefault) {
                            methodFind = item
                            DataOrder.selectedItemMethod = methodFind!!.code
                            return@forEach
                        }
                    }
                    methodFind?.let { ItemRow(it, true) {} }
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
                        if (it < parseAmount && item.code.equals("WALLET")) Text(
                            "${Utils.formatMoney(it)} - Không đủ",
                            style = TextStyle(
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                fontFamily = fontAppDefault,
                                color = colorResource(R.color.yellow)
                            )
                        ) else if (item.code.equals("WALLET")) Text(
                            "Số dư ${Utils.formatMoney(it)}",
                            style = TextStyle(
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                fontFamily = fontAppDefault,
                                color = colorResource(R.color.grey)
                            )
                        )
                    }
                }

                if (DataOrder.balance != null && DataOrder.balance!! < parseAmount && item.code.equals("WALLET")) Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.width(80.dp).height(28.dp).clip(RoundedCornerShape(14.dp))
                        .background(colorResource(R.color.background)).clickableWithoutRipple {
                            NPayLibrary.getInstance().openWallet(Actions.DEPOSIT)
                        }
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

    private fun createOrderWallet(viewModel: InputViewModel, context: Context, appViewModel: AppViewModel) {
        appViewModel.showLoading()
        val params = CreateOrderParamsWallet(
            url = DataOrder.urlData, method = "WALLET"
        )
        CreateOrderWalletRepo().create(context, params, CallbackCreateOrder {
            appViewModel.hideLoading()
            it.message?.let { it1 ->
                if (it.errorCode == 1) {
                    viewModel.showNotification.value = true
                    viewModel.stringDialog.value = it1
                } else if (it.errorCode == 0) {
                    val orderId = com.npsdk.module.utils.Utils.convertUrlToOrderId(it.data!!.redirectUrl!!)
                    appViewModel.showLoading()
                    CreatePayment().create(context, orderId, CallbackCreatePayment { paymentId, message ->
                        run {
                            appViewModel.hideLoading()
                            (context as Activity).finish() // Close input Card
                            if (paymentId == null) {
                                viewModel.showNotification.value = true
                                viewModel.stringDialog.value = message
                                return@run
                            } else {
                                val intent = Intent(context, PasswordActivity::class.java)
                                intent.putExtra("paymentId", paymentId)
                                context.startActivity(intent)
                            }
                        }
                    })
                }
            }
        })
    }
}




