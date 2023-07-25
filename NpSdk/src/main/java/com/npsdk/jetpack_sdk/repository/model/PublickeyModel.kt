package com.npsdk.jetpack_sdk.repository.model

import com.google.gson.annotations.SerializedName

data class PublickeyModel (

    @SerializedName("server_time" ) var serverTime : Int?    = null,
    @SerializedName("status"      ) var status     : Int?    = null,
    @SerializedName("message"     ) var message    : String? = null,
    @SerializedName("error_code"  ) var errorCode  : Int?    = null,
    @SerializedName("data"        ) var data       : KeyData?   = KeyData()

)

data class KeyData (

    @SerializedName("public_key" ) var publicKey : String? = null

)