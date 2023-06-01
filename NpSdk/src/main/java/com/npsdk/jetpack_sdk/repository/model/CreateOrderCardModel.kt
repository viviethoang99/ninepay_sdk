package com.npsdk.jetpack_sdk.repository.model

import com.google.gson.annotations.SerializedName

data class CreateOrderCardModel(

    @SerializedName("server_time") var serverTime: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("error_code") var errorCode: Int? = null,
    @SerializedName("data") var data: Data? = Data()

)

data class Data(
    @SerializedName("status") var status: String? = null,
    @SerializedName("redirect_url") var redirectUrl: String? = null,
    @SerializedName("qr_url") var qrUrl: String? = null
)