package com.npsdk.jetpack_sdk.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var isShowLoading by mutableStateOf(false)

    fun hideLoading() {
        isShowLoading = false
    }

    fun showLoading() {
        isShowLoading = true
    }
}
