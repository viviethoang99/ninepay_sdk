package com.npsdk.jetpack_sdk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.npsdk.jetpack_sdk.base.view.ImageFromUrl
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel
import com.npsdk.module.R

@Composable
fun LineTypeCardInland() {
    val inputViewModel: InputViewModel = viewModel()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Nhập thông tin thẻ",
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W600, fontFamily = fontAppBold)
        )
        Text(
            inputViewModel.inlandBankDetect?.bankCode ?: "", modifier = Modifier.weight(1f), style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                fontFamily = fontAppDefault,
                color = colorResource(R.color.titleText)
            ), textAlign = TextAlign.End
        )
        if (inputViewModel.inlandBankDetect?.logo != null) ImageFromUrl(
            url = inputViewModel.inlandBankDetect!!.logo!!, modifier = Modifier.size(25.dp)
        )

    }
}
