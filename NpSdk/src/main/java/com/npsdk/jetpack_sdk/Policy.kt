package com.npsdk.jetpack_sdk

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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


@Composable
fun PolicyView(callBack: (Boolean) -> Unit) {
    val context = LocalContext.current
    var isChecked by remember {
        mutableStateOf(true)
    }
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = colorResource(R.color.titleText), fontFamily = fontAppDefault, fontSize = 12.sp
            )
        ) {
            append("Tôi đã đọc và đồng ý với ")
        }

        withStyle(
            style = SpanStyle(
                color = colorResource(R.color.green), fontFamily = fontAppDefault, fontSize = 12.sp
            )
        ) {
            appendLink("Điều khoản và chính sách", DataOrder.dataOrderSaved?.data?.policyLink)

        }
    }

    Row(
        modifier = Modifier.padding(top = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp).clip(RoundedCornerShape(4.dp)).border(
            width = 1.dp,
            color = colorResource(if (isChecked) R.color.green else R.color.grey),
            shape = RoundedCornerShape(4.dp)
        ).background(if (!isChecked) Color.White else colorResource(R.color.green)).clickable {
            isChecked = !isChecked
            callBack(isChecked)
        }) {
            if (isChecked) Icon(
                Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp)
            )
        }
        ClickableText(
            modifier = Modifier.weight(1f).padding(start = 16.dp),
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