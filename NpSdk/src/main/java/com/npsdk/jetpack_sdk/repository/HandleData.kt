package com.npsdk.jetpack_sdk.repository

import android.app.Activity
import android.content.Intent
import com.npsdk.jetpack_sdk.DataOrder
import com.npsdk.jetpack_sdk.OrderActivity

fun handleDataFromSDK(activity: Activity, url: String) {
    DataOrder.urlData = url
    val intent = Intent(activity, OrderActivity::class.java)
    activity.startActivity(intent)
}