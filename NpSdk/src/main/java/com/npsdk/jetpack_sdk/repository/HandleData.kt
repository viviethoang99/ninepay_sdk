package com.npsdk.jetpack_sdk.repository

import android.app.Activity
import android.content.Intent
import com.npsdk.jetpack_sdk.OrderActivity
import com.npsdk.jetpack_sdk.urlData

fun handleDataFromSDK(activity: Activity, url: String) {
    urlData = url
    val intent = Intent(activity, OrderActivity::class.java)
    activity.startActivity(intent)
}