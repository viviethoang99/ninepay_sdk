package com.npsdk.jetpack_sdk.repository.model

import com.google.gson.annotations.SerializedName

data class ListBankModel(

    @SerializedName("server_time") var serverTime: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("error_code") var errorCode: Int? = null,
    @SerializedName("data") var data: DataBank? = DataBank()

)

data class DataBank(

    @SerializedName("INLAND") var INLAND: ArrayList<INLAND> = arrayListOf(),
    @SerializedName("INTERNATIONAL") var INTERNATIONAL: ArrayList<INTERNATIONAL> = arrayListOf()

)

data class INLAND(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("short_name") var shortName: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("logo") var logo: String? = null,
    @SerializedName("bank_code") var bankCode: String? = null,
    @SerializedName("prefix") var prefix: Int? = null,
    @SerializedName("is_valid_date") var isValidDate: Int? = null

)

data class INTERNATIONAL(

    @SerializedName("card_brand") var cardBrand: String? = null,
    @SerializedName("icon") var icon: String? = null,
    @SerializedName("prefix") var prefix: ArrayList<String> = arrayListOf(),
    @SerializedName("distance") var distance: ArrayList<ArrayList<String>> = arrayListOf()

)