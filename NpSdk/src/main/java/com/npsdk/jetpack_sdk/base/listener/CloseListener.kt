package com.npsdk.jetpack_sdk.base.listener

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class CloseListener {

    private var broadcastReceiver: BroadcastReceiver? = null
    fun listener(activity: Activity) {
        val filter = IntentFilter()
        filter.addAction("nativeBroadcast")
        handleListener(activity)
        broadcastReceiver?.let {
            LocalBroadcastManager.getInstance(activity).registerReceiver(it, filter)
        }
    }

    private fun handleListener(activity: Activity) {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == "nativeBroadcast") {
                    if (intent.getStringExtra("action").equals("close")) {
                        activity.finish()
                    }
                }
            }
        }
    }

    fun cancelListener(activity: Activity) {
        broadcastReceiver?.let {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(it)
            broadcastReceiver = null
        }
    }
}