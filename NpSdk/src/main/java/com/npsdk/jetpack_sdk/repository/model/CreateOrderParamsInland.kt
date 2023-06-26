package com.npsdk.jetpack_sdk.repository.model

data class CreateOrderParamsInland(
    var url: String? = "",
    var cardNumber: String? = "",
    var cardName: String? = "",
    var expireMonth: String? = "",
    var expireYear: String? = "",
    var amount: String? = "",
    var method: String? = "",
    var isSave: Boolean? = false

    )