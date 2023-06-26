package com.npsdk.jetpack_sdk

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.R
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.theme.initColor


@Composable
fun DescriptionSaveCard() {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = colorResource(R.color.titleText), fontFamily = fontAppDefault, fontSize = 12.sp
            )
        ) {
            append("Lưu lại liên kết thẻ để sử dụng cho những lần thanh toán sau (áp dụng tiêu chuẩn bảo mật PCI DSS, ")
        }

        withStyle(
            style = SpanStyle(
                color = initColor(), fontFamily = fontAppDefault, fontSize = 12.sp
            )
        ) {
            appendLink("tìm hiểu thêm", DataOrder.dataOrderSaved?.data?.policyLink)

        }

        withStyle(
            style = SpanStyle(
                color = colorResource(R.color.titleText), fontFamily = fontAppDefault, fontSize = 12.sp
            )
        ) {
            append(").")
        }
    }

    Row(
        modifier = Modifier.background(colorResource(R.color.background)).padding(top = 12.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ClickableText(
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            text = annotatedString,
            onClick = { offset ->
                annotatedString.onLinkClick(offset) { link ->
                    openPolicy(context, link)
                }
            })
    }

}

private fun AnnotatedString.Builder.appendLink(linkText: String, linkUrl: String?) {
    linkUrl?.let {
        pushStringAnnotation(tag = linkUrl, annotation = linkUrl)
    }
    append(linkText)
    pop()
}

private fun AnnotatedString.onLinkClick(offset: Int, onClick: (String) -> Unit) {
    getStringAnnotations(start = offset, end = offset).firstOrNull()?.let {
        onClick(it.item)
    }
}

private fun openPolicy(context: Context, url: String) {
    val intent = Intent(context, PolicyActivity::class.java)
    intent.putExtra("url", url)
    context.startActivity(intent)
}