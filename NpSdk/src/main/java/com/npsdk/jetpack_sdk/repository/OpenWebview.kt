package com.npsdk.jetpack_sdk.repository

import android.content.Context
import android.content.Intent
import com.npsdk.jetpack_sdk.WebviewComposeActivity

fun openWebviewOTP(context: Context, url: String) {
    val intent = Intent(context, WebviewComposeActivity::class.java)
    intent.putExtra("url", url)
    context.startActivity(intent)
}