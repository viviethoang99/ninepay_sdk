package com.npsdk.jetpack_sdk.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.npsdk.jetpack_sdk.repository.model.validate_payment.Methods

class OrderViewModel : ViewModel() {
    var selectedItemId = mutableStateOf<String?>(null)
    var listMethod = listOf<Methods>()
}