package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.module.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FooterButton(onClickBack: () -> Unit, onClickContinue: () -> Unit, modifier: Modifier) {
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.weight(1f)
				.align(Alignment.Bottom)
				.height(44.dp)
				.clip(shape = RoundedCornerShape(8.dp))
				.border(
					width = 1.dp,
					color = colorResource(id = R.color.green),
					shape = RoundedCornerShape(8.dp),
				)
				.background(Color.White).clickable { onClickBack() }
		) {
			Text(
				text = "Quay lại", style = TextStyle(
					color = colorResource(id = R.color.green),
					fontSize = 14.sp,
					fontWeight = FontWeight.W400,
					fontFamily = fontAppDefault
				)
			)
		}
		Spacer(modifier = Modifier.width(12.dp))
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.clip(shape = RoundedCornerShape(8.dp))
				.height(44.dp)
				.weight(1f)
				.background(colorResource(id = R.color.green)).clickable {
					focusManager.clearFocus()
					keyboardController?.hide()
					onClickContinue() }
		) {
			Text(
				text = "Tiếp tục", style = TextStyle(
					color = colorResource(id = R.color.white),
					fontSize = 14.sp,
					fontWeight = FontWeight.W400,
					fontFamily = fontAppDefault
				)
			)
		}
	}

}