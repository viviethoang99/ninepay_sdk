package com.npsdk.jetpack_sdk

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.npsdk.jetpack_sdk.base.CardNumberMaskCustom
import com.npsdk.jetpack_sdk.base.Validator
import com.npsdk.jetpack_sdk.base.view.BaseDialog
import com.npsdk.jetpack_sdk.base.view.DatePicker
import com.npsdk.jetpack_sdk.base.view.MyEdittext
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardInland(viewModel: InputViewModel) {

    LaunchedEffect(true) {
        // Reset trạng thái.
        viewModel.numberCardInLand.value = ""
        viewModel.numberCardInLand.value = ""

        viewModel.nameCardInLand.value = ""
        viewModel.nameCardErrorInLand.value = ""

        viewModel.dateCardInLand.value = ""
        viewModel.dateCardErrorInLand.value = ""

        viewModel.monthNonParseInLand = null
        viewModel.yearNonParseInLand = null

        viewModel.updateDialogInland(false)
    }

    fun closeDialog() {
        viewModel.updateDialogInland(false)
    }

    Column {
        Spacer(modifier = Modifier.height(24.dp))
        LineTypeCardInland()
        Spacer(modifier = Modifier.height(24.dp))
        MyEdittext("Số thẻ ATM",
            keyboardType = KeyboardType.Number,
            maxLength = 19,
            errText = viewModel.numberCardErrorInLand.value,
            visualTransformation = CardNumberMaskCustom(" "),
            onFocusOut = {
                viewModel.numberCardErrorInLand.value =
                    Validator.validateNumberCardATM(it, inputViewModel = viewModel, showError = true)
            },
            onTextChanged = {
                viewModel.numberCardInLand.value = it
                viewModel.numberCardErrorInLand.value =
                    Validator.validateNumberCardATM(it, inputViewModel = viewModel, showError = false)
                viewModel.dateCardErrorInLand.value = ""
            })
        Spacer(modifier = Modifier.height(12.dp))
        MyEdittext(
            "Họ và tên chủ thẻ",
            onTextChanged = {
                viewModel.nameCardInLand.value = it
            },
            onFocusOut = {
                viewModel.nameCardErrorInLand.value = Validator.validateNameCard(it)
            },
            errText = viewModel.nameCardErrorInLand.value,
        )

        Spacer(modifier = Modifier.height(12.dp))
        MyEdittext(
            label = if (viewModel.inlandBankDetect!!.isValidDate == 1) "Ngày hiệu lực (MM/YY)" else "Ngày hết hạn (MM/YY)",
            keyboardType = KeyboardType.Number,
            maxLength = 5,
            enabled = false,
            onTap = {
                viewModel.updateDialogInland(true)
            },
            initText = viewModel.dateCardInLand.value,
            errText = viewModel.dateCardErrorInLand.value,
        )

        // Dialog
        if (viewModel.openDialogInland.value) BaseDialog(content = {
            DatePicker(onDateSelected = { month, year ->
                kotlin.run {
                    viewModel.monthNonParseInLand = month
                    viewModel.yearNonParseInLand = year
                    var monthStr = "$month"
                    if (monthStr.length == 1) monthStr = "0$month"
                    val year = year.toString().substring(2)
                    viewModel.dateCardInLand.value = "$monthStr/${year}"
                    viewModel.dateCardErrorInLand.value = Validator.validateDateCardInland(
                        viewModel.dateCardInLand.value, viewModel.monthNonParseInLand, viewModel.yearNonParseInLand,
                        inputViewModel = viewModel
                    )
                    closeDialog()
                }
            }, onCancel = {
                closeDialog()
                viewModel.dateCardErrorInLand.value = Validator.validateDateCardInland(
                    viewModel.dateCardInLand.value, viewModel.monthNonParseInLand, viewModel.yearNonParseInLand,
                    inputViewModel = viewModel
                )
            })
        }, onClose = { closeDialog() })
        Spacer(modifier = Modifier.height(12.dp))
    }
}