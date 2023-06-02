package com.npsdk.jetpack_sdk

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.jetpack_sdk.base.Validator
import com.npsdk.jetpack_sdk.base.view.*
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel
import com.steliospapamichail.creditcardmasker.viewtransformations.CardNumberMask
import com.npsdk.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardInternational(viewModel: InputViewModel) {


    LaunchedEffect(true) {
        viewModel.numberOfCardInter.value = ""
        viewModel.numberOfCardErrorInter.value = ""
        viewModel.nameOfCardInter.value = ""
        viewModel.nameOfCardErrorInter.value = ""
        viewModel.expirationDateCardInter.value = ""
        viewModel.expirationDateCardErrorInter.value = ""
        viewModel.cvvCardInter.value = ""
        viewModel.cvvCardErrorInter.value = ""

        viewModel.monthNonParseInter = null
        viewModel.yearNonParseInter = null

        viewModel.updateDialogInter(false)
    }
    fun closeDialog() {
        viewModel.updateDialogInter(false)
    }
    Column {
        LineCardLabel()
        MyEdittext("Số thẻ",
            keyboardType = KeyboardType.Number,
            maxLength = 19,
            tooltipsText = "Nhập đủ 15-19 số in nổi trên thẻ",
            errText = viewModel.numberOfCardErrorInter.value,
            visualTransformation = CardNumberMask(" "),
            onTextChanged = {
                viewModel.numberOfCardInter.value = it
                viewModel.numberOfCardErrorInter.value = Validator.validateNumberCardInter(it, viewModel)
            })
        Spacer(modifier = Modifier.height(12.dp))
        MyEdittext("Họ và tên chủ thẻ",
            tooltipsText = "Nhập tên chủ thẻ không dấu",
            errText = viewModel.nameOfCardErrorInter.value, onTextChanged = {
                viewModel.nameOfCardInter.value = it
                viewModel.nameOfCardErrorInter.value = Validator.validateNameCard(it)
            })
        Spacer(modifier = Modifier.height(12.dp))
        ExpandedRow {

            Row(Modifier.weight(1f)) {
                MyEdittext(
                    "Ngày hết hạn",
                    tooltipsText = "Tháng, năm hết hạn in trên thẻ",
                    keyboardType = KeyboardType.Number,
                    maxLength = 5,
                    enabled = false,
                    onTap = {
                        viewModel.updateDialogInter(true)
                    },
                    initText = viewModel.expirationDateCardInter.value,
                    errText = viewModel.expirationDateCardErrorInter.value,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(Modifier.weight(1f)) {
                MyEdittext("CVC/CVV",
                    keyboardType = KeyboardType.Number,
                    maxLength = 4,
                    tooltipsText = "3 số CVC/CVV in ở mặt sau thẻ",
                    errText = viewModel.cvvCardErrorInter.value,
                    onTextChanged = {
                        viewModel.cvvCardInter.value = it
                        viewModel.cvvCardErrorInter.value = Validator.validateCCVCard(it)
                    })
            }
        }

        if (viewModel.openDialogInter.value) BaseDialog(content = {
            DatePicker(onDateSelected = { month, year ->
                kotlin.run {
                    viewModel.monthNonParseInter = month
                    viewModel.yearNonParseInter = year
                    var monthStr = "$month"
                    if (monthStr.length == 1) monthStr = "0$month"
                    val year = year.toString().substring(2)
                    viewModel.expirationDateCardInter.value = "$monthStr/${year}"
                    viewModel.expirationDateCardErrorInter.value = Validator.validateExpirationCard(
                        viewModel.expirationDateCardInter.value,
                        viewModel.monthNonParseInter,
                        viewModel.yearNonParseInter
                    )
                    closeDialog()
                }
            }, onCancel = {
                closeDialog()
                viewModel.expirationDateCardErrorInter.value = Validator.validateExpirationCard(
                    viewModel.expirationDateCardInter.value, viewModel.monthNonParseInter, viewModel.yearNonParseInter
                )
            })
        }, onClose = { closeDialog() })

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun LineCardLabel() {
    val inputViewModel: InputViewModel = viewModel()
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Nhập thông tin thẻ",
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W600, fontFamily = fontAppBold)
        )
        if ((inputViewModel.interCardDetect?.cardBrand ?: "").isBlank()) Image(
            modifier = Modifier.width(141.dp).height(37.dp),
            painter = painterResource(R.drawable.card_label),
            contentDescription = null
        )

        if ((inputViewModel.interCardDetect?.cardBrand ?: "").isNotBlank()) Box(
            contentAlignment = Alignment.Center, modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(
                Color.White
            ).padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            ImageFromUrl(
                url = inputViewModel.interCardDetect!!.icon!!,
                modifier = Modifier.width(22.dp),
            )
        }
    }


}