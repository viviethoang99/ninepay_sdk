package com.npsdk.jetpack_sdk.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.npsdk.module.NPayLibrary
import com.npsdk.jetpack_sdk.WebviewActivity

fun openWebviewSdk(context: Context, url: String? = "") {
    if (NPayLibrary.getInstance().listener != null) {
        NPayLibrary.getInstance().pay(url)
    } else {
        Toast.makeText(context, "Bạn chưa khởi tạo SDK", Toast.LENGTH_SHORT ).show()
    }
}

fun openWebviewOTP(context: Context, url: String) {
    val intent = Intent(context, WebviewActivity::class.java)
    intent.putExtra("url", url)
    context.startActivity(intent)
}