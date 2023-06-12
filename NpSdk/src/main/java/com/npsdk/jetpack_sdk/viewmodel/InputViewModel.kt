package com.npsdk.jetpack_sdk.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.npsdk.jetpack_sdk.repository.model.INLAND
import com.npsdk.jetpack_sdk.repository.model.INTERNATIONAL

class InputViewModel : ViewModel() {
    // Inland

    var numberCardInLand = mutableStateOf("")
    var numberCardErrorInLand = mutableStateOf("")

    var nameCardInLand = mutableStateOf("")
    var nameCardErrorInLand = mutableStateOf("")

    var dateCardInLand = mutableStateOf("")
    var dateCardErrorInLand = mutableStateOf("")


    var monthNonParseInLand: Int? = null
    var yearNonParseInLand: Int? = null

    var _openDialogInland = mutableStateOf(false)
    val openDialogInland: State<Boolean> = _openDialogInland

    var showNotification = mutableStateOf(false)
    var stringDialog = mutableStateOf("")

    var inlandBankDetect by mutableStateOf<INLAND?>(INLAND())

    fun updateDialogInland(state: Boolean?) {
        if (state != null) {
            _openDialogInland.value = state
        } else {
            _openDialogInland.value = !_openDialogInland.value
        }
    }

// End inland


// International

    var numberOfCardInter = mutableStateOf("")
    var numberOfCardErrorInter = mutableStateOf("")
    var nameOfCardInter = mutableStateOf("")
    var nameOfCardErrorInter = mutableStateOf("")
    var expirationDateCardInter = mutableStateOf("")
    var expirationDateCardErrorInter = mutableStateOf("")
    var cvvCardInter = mutableStateOf("")
    var cvvCardErrorInter = mutableStateOf("")

    var monthNonParseInter: Int? = null
    var yearNonParseInter: Int? = null

    var _openDialogInter = mutableStateOf(false)
    var openDialogInter: State<Boolean> = _openDialogInter

    fun updateDialogInter(state: Boolean? = false) {
        if (state != null) {
            _openDialogInter.value = state
        } else {
            _openDialogInter.value = !_openDialogInter.value
        }
    }

    var interCardDetect by mutableStateOf<INTERNATIONAL?>(null)


// End International
}