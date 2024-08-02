package com.npsdk.jetpack_sdk.base

import com.npsdk.jetpack_sdk.DataOrder
import com.npsdk.jetpack_sdk.repository.model.INLAND
import com.npsdk.jetpack_sdk.repository.model.INTERNATIONAL
import com.npsdk.jetpack_sdk.viewmodel.InputViewModel
import java.math.BigInteger

object Validator {

    fun validateNumberCardInter(
        input: String,
        inputViewModel: InputViewModel,
        showError: Boolean? = true
    ): String {
        var stringError = ""

        if (input.isBlank()) {
            inputViewModel.interCardDetect = null
        }
        if (showError!!) {
            when {
                input.isBlank() -> stringError = "Số thẻ không được để trống."
                input.length < 15 -> stringError = "Số thẻ không hợp lệ. Vui lòng kiểm tra lại."
            }
        }

        DataOrder.listBankModel?.data?.INTERNATIONAL?.isNotEmpty().let {
            if (input.isNotBlank()) {
                val isMatchPrefix =
                    checkMatchPrefixInter(
                        DataOrder.listBankModel!!.data!!.INTERNATIONAL,
                        input,
                        inputViewModel
                    )

                // Cả 2 điệu kiện điều không thỏa mãn
                if (!isMatchPrefix) {
                    val isMatchDistance =
                        checkMatchDistanceInter(
                            DataOrder.listBankModel!!.data!!.INTERNATIONAL,
                            input.toBigInteger(),
                            inputViewModel
                        )
                    if (showError!!) {
                        if (!isMatchDistance && input.length >= 6) stringError =
                            "Thẻ này chưa được hỗ trợ thanh toán. Vui lòng thanh toán thẻ khác."
                    }
                }
            }
        }
        return stringError
    }

    fun validateNumberCardATM(
        input: String,
        inputViewModel: InputViewModel,
        showError: Boolean? = true
    ): String {
        var stringError = ""

        if (showError!!) {
            when {
                input.isBlank() -> stringError = "Vui lòng nhập số thẻ ATM."
                input.length < 15 -> stringError = "Số thẻ ATM không hợp lệ. Vui lòng kiểm tra lại."
            }
        }


        DataOrder.listBankModel?.data?.INLAND?.let {
            var isSupport = false
            for (item in it) {
                if (input.startsWith(item.prefix.toString())) {
                    isSupport = true
                    inputViewModel.inlandBankDetect = item
                    break
                } else {
                    inputViewModel.inlandBankDetect = INLAND(isValidDate = 1)
                }
            }
            if (showError!!) if (!isSupport) stringError =
                "Số thẻ ATM không hợp lệ. Vui lòng kiểm tra lại."
        }

        return stringError
    }

    fun validateNameCard(input: String): String {
        var stringError = ""

        when {
            input.isBlank() -> stringError = "Vui lòng nhập Họ và tên."
            input.length < 6 || input.endsWith(" ") -> stringError =
                "Họ và tên chủ thẻ không hợp lệ. Vui lòng kiểm tra lại."
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

    fun validateDateCardInland(
        input: String,
        month: Int?,
        year: Int?,
        inputViewModel: InputViewModel
    ): String {

        // 1 Ngay hieu luc, 0 ngay het han
        val isEffectiveCard: Boolean = inputViewModel.inlandBankDetect?.isValidDate == 1
        val typeString = if (isEffectiveCard) "hiệu lực" else "hết hạn"
        var stringError = ""

        when {
            input.isBlank() -> stringError = "Vui lòng nhập ngày $typeString."
            input.length != 5 -> stringError =
                "Ngày $typeString không hợp lệ. Vui lòng kiểm tra lại."

        }

        val (monthCurrent, yearCurrent) = AppUtils.getMonthYearCurrent()
        year?.let {
            if (isEffectiveCard) {
                // Hieu luc
                if (year > yearCurrent || (year == yearCurrent && month!! > monthCurrent)) stringError =
                    "Ngày hiệu lực không hợp lệ. Vui lòng kiểm tra lại."
            } else {
                // Het han
                if (year < yearCurrent || (year == yearCurrent && month!! < monthCurrent)) stringError =
                    "Ngày hết hạn không hợp lệ. Vui lòng kiểm tra lại."
            }
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
            val (monthCurrent, yearCurrent) = AppUtils.getMonthYearCurrent()
            if (year < yearCurrent || (year == yearCurrent && month!! < monthCurrent)) {
                stringError = "Ngày hết hạn không hợp lệ. Vui lòng kiểm tra lại."
            }
        }
        return stringError
    }

    private fun checkMatchPrefixInter(
        list: ArrayList<INTERNATIONAL>, prefixString: String, inputViewModel: InputViewModel
    ): Boolean {
        var isMatch = false
        inputViewModel.interCardDetect = null
        var currentCardInterMatch: INTERNATIONAL? = null
        list.forEach { element ->
            var itemListPrefix: ArrayList<String> = element.prefix
            itemListPrefix.forEach { item ->
                if (prefixString.startsWith(item)) {
                    currentCardInterMatch = element
                    return@forEach
                }
            }
        }
        currentCardInterMatch?.cardBrand?.let {
            inputViewModel.interCardDetect = currentCardInterMatch
            DataOrder.dataOrderSaved?.data?.allowedCreditCardBrand?.let { listAllow ->
                if (listAllow.contains(currentCardInterMatch?.cardBrand)) {
                    if (prefixString.length >= 6) {
                        isMatch = filterFeeInter(it, prefixString)
                    }
                }
            }
        }
        return isMatch
    }

    private fun filterFeeInter(nameCard: String, prefixString: String): Boolean {
        val listFeeBank = DataOrder.listBankModel?.data?.INTERNATIONALINLAND
        if (listFeeBank != null) {
            val listCreateCard = DataOrder.dataOrderSaved?.data?.feeData?.creditCard
            val binLocaleAllow = DataOrder.dataOrderSaved!!.data!!.merchantInfo!!.binLocaleAllow
            // value = 99 thì cả IN và OUTLAND, = 1 thì INLAND, = 2 OUTLAND
            val splitPrefix: String =
                if (prefixString.length > 6) prefixString.substring(0, 6) else prefixString
            val createCard = listCreateCard?.single { it.cardBrand == nameCard }

//            val x = listFeeBank.contains(splitPrefix)
//            val y = intArrayOf(99, 1).contains(binLocaleAllow)

            if (intArrayOf(99, 1).contains(binLocaleAllow) && listFeeBank.contains(splitPrefix)) {
                DataOrder.totalAmount = createCard?.inLand
                return true
            } else if (intArrayOf(99, 2).contains(binLocaleAllow)) {
                DataOrder.totalAmount = createCard?.outLand
                return true
            }
        }
        return false
    }

    private fun checkMatchDistanceInter(
        list: ArrayList<INTERNATIONAL>, prefixNumber: BigInteger, inputViewModel: InputViewModel
    ): Boolean {
        var isMatch = false
        var currentCardMatch: INTERNATIONAL? = null
        list.forEach { element ->
            var itemByTypeDistance = element.distance

            itemByTypeDistance.forEach { itemDistance ->
                val start: BigInteger = itemDistance.first().toBigInteger()
                val end: BigInteger = itemDistance.last().toBigInteger()
                // Chỉ so sánh trong 1 khoảng đủ số ký tự đầu mà model có.
                if (prefixNumber.toString().length >= start.toString().length) {
                    var numberSplit: BigInteger =
                        (prefixNumber.toString()
                            .substring(0, start.toString().length)).toBigInteger()
                    if (numberSplit in start..end) {
                        currentCardMatch = element
                        return@forEach
                    }
                }

            }
        }
        currentCardMatch?.cardBrand?.let {
            inputViewModel.interCardDetect = currentCardMatch
            DataOrder.dataOrderSaved?.data?.allowedCreditCardBrand?.let { listAllow ->
                if (listAllow.contains(currentCardMatch?.cardBrand)) {
                    if (prefixNumber.toString().length >= 6) {
                        isMatch = filterFeeInter(it, prefixNumber.toString())
                    }
                }
            }
        }
        return isMatch
    }
}