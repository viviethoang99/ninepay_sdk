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

        viewModel.effectiveCardInLand.value = ""
        viewModel.effectiveCardErrorInLand.value = ""

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
            onTextChanged = {
                viewModel.numberCardInLand.value = it
                viewModel.numberCardErrorInLand.value = Validator.validateNumberCardATM(it, inputViewModel = viewModel)
            })
        Spacer(modifier = Modifier.height(12.dp))
        MyEdittext("Họ và tên chủ thẻ", onTextChanged = {
            viewModel.nameCardInLand.value = it
            viewModel.nameCardErrorInLand.value = Validator.validateNameCard(it)
        }, errText = viewModel.nameCardErrorInLand.value)
        Spacer(modifier = Modifier.height(12.dp))
        MyEdittext(
            "Ngày hiệu lực (MM/YY)",
            keyboardType = KeyboardType.Number,
            maxLength = 5,
            enabled = false,
            onTap = {
                viewModel.updateDialogInland(true)
            },
            initText = viewModel.effectiveCardInLand.value,
            errText = viewModel.effectiveCardErrorInLand.value,
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
                    viewModel.effectiveCardInLand.value = "$monthStr/${year}"
                    viewModel.effectiveCardErrorInLand.value = Validator.validateEffectiveCard(
                        viewModel.effectiveCardInLand.value, viewModel.monthNonParseInLand, viewModel.yearNonParseInLand
                    )
                    closeDialog()
                }
            }, onCancel = {
                closeDialog()
                viewModel.effectiveCardErrorInLand.value = Validator.validateEffectiveCard(
                    viewModel.effectiveCardInLand.value, viewModel.monthNonParseInLand, viewModel.yearNonParseInLand
                )
            })
        }, onClose = { closeDialog() })
        Spacer(modifier = Modifier.height(12.dp))
    }

}