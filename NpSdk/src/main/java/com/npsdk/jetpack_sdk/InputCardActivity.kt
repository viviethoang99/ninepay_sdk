package com.npsdk.jetpack_sdk

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.Validator
import com.npsdk.jetpack_sdk.base.view.*
import com.npsdk.jetpack_sdk.repository.*
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsInland
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsInter
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.jetpack_sdk.viewmodel.AppViewModel
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel

class InputCardActivity : ComponentActivity() {

    private var method: String? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent.extras
        if (intent != null) {
            method = intent.getString("method")
        }
        setContent {
            PaymentNinepayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = {
                        TopAppBarApp()
                    }, content = { paddingValues ->
                        Body(paddingValues = paddingValues)
                    })
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun Body(paddingValues: PaddingValues?) {
        val inputViewModel: InputViewModel = viewModel()
        val appViewModel: AppViewModel = viewModel()
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        var isSelectedPolicy = true

        LaunchedEffect(true) {
            if (DataOrder.listBankModel == null) {
                appViewModel.showLoading()
                GetListBank().get(context) { response ->
                    appViewModel.hideLoading()
                    DataOrder.listBankModel = response
                }
            }
            if (DataOrder.dataOrderSaved == null) {
                CheckValidatePayment().check(context, DataOrder.urlData, CallbackOrder { data ->
                    DataOrder.dataOrderSaved = data
                    DataOrder.amount =
                        (DataOrder.dataOrderSaved!!.data.listPaymentData.find { it.name.equals("Giá trị đơn hàng") }?.value)
                })
            }
        }
        Box(
            modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.background))
                .padding(top = paddingValues!!.calculateTopPadding()).clickableWithoutRipple {
                    context.let {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }

                }, contentAlignment = Alignment.TopStart
        ) {
            LazyColumn(
                modifier = Modifier.padding(
                    start = 16.dp, end = 16.dp, top = 16.dp, bottom = 60.dp
                )
            ) {
                item {
                    if (DataOrder.dataOrderSaved != null) HeaderOrder(DataOrder.dataOrderSaved!!)
                }

                item {
                    if (method == "ATM_CARD") CardInland(viewModel = inputViewModel) else if (method == "CREDIT_CARD") CardInternational(
                        viewModel = inputViewModel
                    )
                }

                item {
                    if (inputViewModel.showNotification.value) {
                        DialogNotification(contextString = inputViewModel.stringDialog.value, onDismiss = {
                            inputViewModel.showNotification.value = false
                        })
                    }
                }

                item {
                    if (method == "CREDIT_CARD" && DataOrder.dataOrderSaved != null) PolicyView(callBack = {
                        isSelectedPolicy = it
                    })
                }

                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }

            }

            if (appViewModel.isShowLoading) Box(Modifier.align(Alignment.Center)) {
                LoadingView()
            }
            FooterButton(onClickBack = {
                (context as Activity).finish()
            }, onClickContinue = {
                if (!isSelectedPolicy) {
                    inputViewModel.showNotification.value = true
                    inputViewModel.stringDialog.value = "Bạn phải chấp nhận với điều khoản để tiếp tục."
                    return@FooterButton
                }

                DataOrder.activityOrder?.finish() // Close order activity

                when {
                    method == "CREDIT_CARD" -> createOrderInternational(inputViewModel, context, appViewModel)
                    method == "ATM_CARD" -> createOrderInland(inputViewModel, context, appViewModel)
                }

            }, modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(12.dp)
            )
        }
    }
}


fun createOrderInternational(viewModel: InputViewModel, context: Context, appViewModel: AppViewModel) {

    val numberCard = viewModel.numberOfCardInter.value
    val nameCard = viewModel.nameOfCardInter.value
    val exp = viewModel.expirationDateCardInter.value
    val ccv = viewModel.cvvCardInter.value

    viewModel.numberOfCardErrorInter.value = Validator.validateNumberCardInter(numberCard, viewModel)
    viewModel.nameOfCardErrorInter.value = Validator.validateNameCard(nameCard)
    viewModel.expirationDateCardErrorInter.value = Validator.validateExpirationCard(
        exp, viewModel.monthNonParseInter, viewModel.yearNonParseInter
    )
    viewModel.cvvCardErrorInter.value = Validator.validateCCVCard(ccv)

    if (viewModel.numberOfCardErrorInter.value.isNotBlank()) {
        return
    }
    if (viewModel.numberOfCardErrorInter.value.isNotBlank()) {
        return
    }

    if (viewModel.expirationDateCardErrorInter.value.isNotBlank()) {
        return
    }

    if (viewModel.cvvCardErrorInter.value.isNotBlank()) {
        return
    }

    val expCardStr = viewModel.expirationDateCardInter.value.split("/")
    val month: String = expCardStr.first()
    val year: String = expCardStr[1]
    var amount: Any? = DataOrder.amount
    if (amount is Double) amount = amount.toInt()
    val params = CreateOrderParamsInter(
        url = DataOrder.urlData,
        cardNumber = viewModel.numberOfCardInter.value,
        cardName = viewModel.nameOfCardInter.value,
        expireMonth = month,
        expireYear = year,
        cvc = viewModel.cvvCardInter.value,
        amount = amount.toString(),
        method = "CREDIT_CARD"
    )

    appViewModel.showLoading()
    CreateOrderInterRepo().create(context, params, CallbackCreateOrder {
        appViewModel.hideLoading()
        it.message?.let { it1 ->
            if (it.errorCode == 1) {
                viewModel.showNotification.value = true
                viewModel.stringDialog.value = it1
            } else if (it.errorCode == 0) {
                (context as Activity).finish() // Close input card
                openWebviewOTP(context, it.data!!.redirectUrl!!)
            }
        }

    })

}

fun createOrderInland(viewModel: InputViewModel, context: Context, appViewModel: AppViewModel) {

    val numberCard = viewModel.numberCardInLand.value
    val nameCard = viewModel.nameCardInLand.value
    val dateCard = viewModel.dateCardInLand.value

    viewModel.numberCardErrorInLand.value = Validator.validateNumberCardATM(numberCard, inputViewModel = viewModel)
    viewModel.nameCardErrorInLand.value = Validator.validateNameCard(nameCard)
    viewModel.dateCardErrorInLand.value = Validator.validateDateCardInland(
        viewModel.dateCardInLand.value, viewModel.monthNonParseInLand, viewModel.yearNonParseInLand, viewModel
    )

    if (viewModel.numberCardErrorInLand.value.isNotBlank()) {
        return
    }
    if (viewModel.nameCardErrorInLand.value.isNotBlank()) {
        return
    }


    if (viewModel.dateCardErrorInLand.value.isNotBlank()) {
        return
    }

    val expCardStr = dateCard.split("/")
    val month: String = expCardStr.first()
    val year: String = expCardStr[1]
    var amount: Any? = DataOrder.amount
    if (amount is Double) amount = amount.toInt()
    val params = CreateOrderParamsInland(
        url = DataOrder.urlData,
        cardNumber = numberCard,
        cardName = nameCard,
        expireMonth = month,
        expireYear = year,
        amount = amount.toString(),
        method = "ATM_CARD"
    )

    appViewModel.showLoading()
    CreateOrderInlandRepo().create(context, params, CallbackCreateOrder {
        appViewModel.hideLoading()
        it.message?.let { it1 ->
            if (it.errorCode == 1) {
                viewModel.showNotification.value = true
                viewModel.stringDialog.value = it1
            } else if (it.errorCode == 0) {
                (context as Activity).finish() // Close input card
                openWebviewOTP(context, it.data!!.redirectUrl!!)
            }
        }
    })

}