package com.npsdk.jetpack_sdk.repository.model

import com.google.gson.annotations.SerializedName

data class MerchantModel (
    @SerializedName("server_time" ) var serverTime : Int?    = null,
    @SerializedName("status"      ) var status     : Int?    = null,
    @SerializedName("message"     ) var message    : String? = null,
    @SerializedName("error_code"  ) var errorCode  : Int?    = null,
    @SerializedName("data"        ) var data       : MerchantData?   = MerchantData()
)

data class MerchantData (
    @SerializedName("merchant_info" ) var merchantInfo : MerchantInfo? = MerchantInfo()
)
data class MerchantInfo (
    @SerializedName("merchant_code"    ) var merchantCode    : String? = null,
    @SerializedName("merchant_name"    ) var merchantName    : String? = null,
    @SerializedName("logo"             ) var logo            : String? = null,
    @SerializedName("background_login" ) var backgroundLogin : String? = null,
    @SerializedName("loading_logo"     ) var loadingLogo     : String? = null
)