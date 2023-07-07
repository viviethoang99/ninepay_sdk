package com.npsdk.jetpack_sdk.repository.model

import com.google.gson.annotations.SerializedName


data class PaymentModel (

    @SerializedName("server_time" ) var serverTime : Int?    = null,
    @SerializedName("status"      ) var status     : Int?    = null,
    @SerializedName("message"     ) var message    : String? = null,
    @SerializedName("error_code"  ) var errorCode  : Int?    = null,
    @SerializedName("data"        ) var data       : DataPayment?   = DataPayment()

)

data class DataPayment (

    @SerializedName("transaction_id" ) var transactionId : String? = null,
    @SerializedName("payment_id"     ) var paymentId     : String? = null,
    @SerializedName("amount"         ) var amount        : Int?    = null,
    @SerializedName("fee"            ) var fee           : Int?    = null,
    @SerializedName("discount"       ) var discount      : Int?    = null,
    @SerializedName("voucher"        ) var voucher       : Int?    = null,
    @SerializedName("total"          ) var total         : Int?    = null,
    @SerializedName("redirect_url"   ) var redirectUrl   : String? = null,
    @SerializedName("status"         ) var status        : Int?    = null,
    @SerializedName("verify_type"    ) var verifyType    : String? = null,
    @SerializedName("otp_type"       ) var otpType       : Int?    = null,
    @SerializedName("otp_message"    ) var otpMessage    : String? = null

)