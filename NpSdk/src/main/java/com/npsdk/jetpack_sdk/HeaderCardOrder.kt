package com.npsdk.jetpack_sdk

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.Utils.formatMoney
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import kotlin.math.roundToInt


@Composable
private fun BoxCollapse(data: ValidatePaymentModel, onClick: () -> Unit) {
    var description: String = data.data.listPaymentData.find { it.name.equals("description") }?.value ?: ""
    var amount: Int? = (data.data.listPaymentData.find { it.name.equals("amount") }?.value as String).toInt()
    Box(
        modifier = Modifier.fillMaxWidth().clip(shape = RoundedCornerShape(12.dp)).background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = description, style = TextStyle(
                    fontWeight = FontWeight.W400, color = colorResource(
                        id = R.color.titleText
                    ), fontSize = 12.sp, fontFamily = fontAppDefault
                )
            )
            amount?.let {
                Text(
                    text = formatMoney(it),
                    style = TextStyle(
                        fontWeight = FontWeight.W600, fontSize = 18.sp, fontFamily = fontAppBold
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                Modifier.height(1.dp).fillMaxWidth().background(Color.Gray, shape = DottedShape(step = 5.dp))
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp).height(40.dp).clickable {
                    onClick()
                }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Xem thêm", textAlign = TextAlign.Center, style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontFamily = fontAppBold,
                        color = Color(0xFF1F92FC),
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painterResource(R.drawable.arrow_down),
                    modifier = Modifier.size(10.dp),
                    contentDescription = null,
                )
            }


        }
    }
}

//@Preview(showBackground = true)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HeaderOrder(data: ValidatePaymentModel) {
//    val labelItem: ArrayList<LabelItem> = arrayListOf()
    val paymentData = data.data.listPaymentData
    val merchantInfo = data.data.merchantInfo

//    val invoiceNo = paymentData.invoiceNo
//    val nameMerchant = merchantInfo.name
//    val description = paymentData.description
//    val amout = Utils.formatMoney(paymentData.amount)
//    labelItem.add(LabelItem("Mã giao dịch", invoiceNo))
//    labelItem.add(LabelItem("Đơn vị cung cấp", nameMerchant))
//    labelItem.add(LabelItem("Nội dung", description))
//    labelItem.add(LabelItem("Giá trị đơn hàng", amout))
//    labelItem.add(LabelItem("Phí giao dịch", ""))

    var isExpanded by remember { mutableStateOf(true) }


    AnimatedContent(targetState = isExpanded) { showBoxCollapse ->
        if (showBoxCollapse) BoxCollapse(data = data) { ->
            isExpanded = !isExpanded
        } else Box(
            modifier = Modifier.fillMaxWidth().clip(shape = RoundedCornerShape(12.dp)).background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
            ) {
                data.data.listPaymentData.map { rowItem ->
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            rowItem.name,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                color = colorResource(id = R.color.titleText),
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                fontFamily = fontAppDefault
                            )
                        )

                        Text(
                            text = rowItem.value,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            style = TextStyle(fontFamily = fontAppBold, fontSize = 12.sp)

                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    Modifier.height(1.dp).fillMaxWidth().background(Color.Gray, shape = DottedShape(step = 5.dp))
                )

                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp).padding(top = 8.dp, bottom = 6.dp).clickable {
                        isExpanded = !isExpanded
                    }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Thu gọn", textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontFamily = fontAppBold,
                            color = Color(0xFF1F92FC),
                            fontSize = 12.sp
                        ),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painterResource(R.drawable.arrow_up),
                        modifier = Modifier.size(10.dp),
                        contentDescription = null,
                    )
                }

            }
        }

    }


}

data class LabelItem(
    val name: String,
    val value: String,
)

private data class DottedShape(
    val step: Dp,
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ) = Outline.Generic(Path().apply {
        val stepPx = with(density) { step.toPx() }
        val stepsCount = (size.width / stepPx).roundToInt()
        val actualStep = size.width / stepsCount
        val dotSize = Size(width = actualStep / 2, height = size.height)
        for (i in 0 until stepsCount) {
            addRect(
                Rect(
                    offset = Offset(x = i * actualStep, y = 0f), size = dotSize
                )
            )
        }
        close()
    })
}