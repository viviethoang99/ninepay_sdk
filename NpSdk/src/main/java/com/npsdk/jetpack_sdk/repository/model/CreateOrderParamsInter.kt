package com.npsdk.jetpack_sdk.repository.model

data class CreateOrderParamsInter(
    var url: String? = "",
    var cardNumber: String? = "",
    var cardName: String? = "",
    var expireMonth: String? = "",
    var expireYear: String? = "",
    var cvc: String? = "",
    var amount: String? = "",
    var method: String? = "",
    var saveToken: Int? = 0
    )