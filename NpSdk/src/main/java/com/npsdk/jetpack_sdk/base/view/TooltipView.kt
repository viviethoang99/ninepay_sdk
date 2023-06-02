package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.npsdk.R

@Preview(showBackground = true)
@Composable
fun TooltipView(tooltipTitle: String? = "", tooltipContent: String? = "") {
    val builder = rememberBalloonBuilder {
        setArrowSize(10)
        setArrowPosition(0.5f)
        setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPadding(12)
        setMarginHorizontal(12)
        setCornerRadius(8f)
        setBackgroundColorResource(R.color.white)
        setBalloonAnimation(BalloonAnimation.FADE)
    }

    if (!tooltipContent.isNullOrBlank()) Balloon(builder = builder, balloonContent = {
        Column {
            Text(
                text = tooltipTitle!!, style = TextStyle(
                    color = colorResource(R.color.titleText),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = fontAppBold
                )
            )
            Text(
                text = tooltipContent, style = TextStyle(
                    color = colorResource(R.color.titleText),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = fontAppDefault
                )
            )
        }
    }) { balloonWindow ->
        Image(
            contentScale = ContentScale.FillWidth, modifier = Modifier.width(18.dp).clickableWithoutRipple {
                balloonWindow.showAsDropDown()
            }, painter = painterResource(R.drawable.icon_info), contentDescription = null
        )
    }

}