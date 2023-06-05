package com.npsdk.jetpack_sdk.base

import com.npsdk.jetpack_sdk.DataOrder
import com.npsdk.jetpack_sdk.repository.model.INTERNATIONAL
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel

object Validator {

    fun validateNumberCardInter(input: String, inputViewModel: InputViewModel): String {
        var stringError = ""

        if (input.isBlank()) {
            inputViewModel.interCardDetect = null
        }
        when {
            input.isBlank() -> stringError = "Số thẻ không được để trống."
            input.length < 16 -> stringError = "Số thẻ không hợp lệ. Vui lòng kiểm tra lại."
        }

        DataOrder.listBankModel?.data?.INTERNATIONAL?.isNotEmpty().let {
            if (input.isNotBlank()) {
                val isMatchPrefix = checkMatchPrefixInter(DataOrder.listBankModel!!.data!!.INTERNATIONAL, input, inputViewModel)

                // Cả 2 điệu kiện điều không thỏa mãn
                if (!isMatchPrefix) {
                    val isMatchDistance =
                        checkMatchDistanceInter(DataOrder.listBankModel!!.data!!.INTERNATIONAL, input.toLong(), inputViewModel)
                    if (!isMatchDistance) stringError =
                        "Thẻ này chưa được hỗ trợ thanh toán. Vui lòng thanh toán thẻ khác."
                }
            }
        }
        return stringError
    }

    fun validateNumberCardATM(input: String, inputViewModel: InputViewModel): String {
        var stringError = ""

        when {
            input.isBlank() -> stringError = "Vui lòng nhập số thẻ ATM."
            input.length < 10 -> stringError = "Số thẻ ATM không hợp lệ. Vui lòng kiểm tra lại."
        }


        DataOrder.listBankModel?.data?.INLAND?.let {
            var isSupport = false
            for (item in it) {
                if (input.startsWith(item.prefix.toString())) {
                    isSupport = true
                    inputViewModel.inlandBankDetect = item
                    break
                } else {
                    inputViewModel.inlandBankDetect = null
                }
            }
            if (!isSupport) stringError = "Số thẻ ATM không hợp lệ. Vui lòng kiểm tra lại."
        }

        return stringError
    }

    fun validateNameCard(input: String): String {
        var stringError = ""

        when {
            input.isBlank() -> stringError = "Vui lòng nhập Họ và tên."
            input.length < 5 -> stringError = "Họ và tên chủ thẻ không hợp lệ. Vui lòng kiểm tra lại."
        }

        return stringError
    }

    fun validateCCVCard(input: String): String {
        var stringError = ""

        when {
            input.isBlank() -> stringError = "CVC/CCV không được để trống."
            input.length < 3 -> stringError = "CVC/CCV không hợp lệ."
        }

        return stringError
    }

    fun validateEffectiveCard(input: String, month: Int?, year: Int?): String {
        var stringError = ""

        when {
            input.isBlank() -> stringError = "Vui lòng nhập ngày hiệu lực."
            input.length != 5 -> stringError = "Ngày hiệu lực không hợp lệ. Vui lòng kiểm tra lại."

        }

        val (monthCurrent, yearCurrent) = Utils.getMonthYearCurrent()
        year?.let {
            if (year > yearCurrent || (year == yearCurrent && month!! > monthCurrent)) stringError =
                "Ngày hiệu lực không hợp lệ. Vui lòng kiểm tra lại."
        }
        return stringError
    }

    fun validateExpirationCard(input: String, month: Int?, year: Int?): String {
        var stringError = ""

        when {
            input.isBlank() -> stringError = "Vui lòng nhập ngày hết hạn."
            input.length != 5 -> stringError = "Ngày hết hạn không hợp lệ. Vui lòng kiểm tra lại."

        }

        year?.let {
            val (monthCurrent, yearCurrent) = Utils.getMonthYearCurrent()
            if (year < yearCurrent || (year == yearCurrent && month!! < monthCurrent)) {
                stringError = "Ngày hết hạn không hợp lệ. Vui lòng kiểm tra lại."
            }
        }
        return stringError
    }

    private fun checkMatchPrefixInter(
        list: ArrayList<INTERNATIONAL>, prefixString: String, inputViewModel: InputViewModel
    ): Boolean {
        var isMatch: Boolean = false
        inputViewModel.interCardDetect = null
        var currentCardInterMatch: INTERNATIONAL? = null
        list.forEach { element ->
            var itemListPrefix: ArrayList<String> = element.prefix
            itemListPrefix.forEach { item ->
                if (prefixString.startsWith(item)) {
                    isMatch = true
                    currentCardInterMatch = element
                    return@forEach
                }
            }
        }
        currentCardInterMatch?.cardBrand?.let {
            inputViewModel.interCardDetect = currentCardInterMatch
        }
        return isMatch
    }


    private fun checkMatchDistanceInter(
        list: ArrayList<INTERNATIONAL>, prefixNumber: Long, inputViewModel: InputViewModel
    ): Boolean {
        var isMatch: Boolean = false
        inputViewModel.interCardDetect = null
        var currentCardMatch: INTERNATIONAL? = null
        list.forEach { element ->
            var itemByTypeDistance = element.distance

            itemByTypeDistance.forEach { itemDistance ->
                val start: Int = itemDistance.first().toInt()
                val end: Int = itemDistance.last().toInt()
                // Chỉ so sánh trong 1 khoảng đủ số ký tự đầu mà model có.
                if (prefixNumber.toString().length >= start.toString().length) {
                    var numberSplit: Long = (prefixNumber.toString().substring(0, start.toString().length)).toLong()
                    if (numberSplit in start..end) {
                        isMatch = true
                        currentCardMatch = element
                        return@forEach
                    }
                }

            }
        }
        currentCardMatch?.cardBrand?.let {
            inputViewModel.interCardDetect = currentCardMatch
        }
        return isMatch
    }
}