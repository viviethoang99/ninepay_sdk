package com.npsdk.jetpack_sdk.base

import android.net.Uri
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun getMonthCurrent(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1
    }

    fun getYearCurrent(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getMonthYearCurrent(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return month to year
    }

    fun formatMoney(amount: Any): String {
        val decimalFormatSymbols = DecimalFormatSymbols(Locale("vi", "VN"))
        decimalFormatSymbols.groupingSeparator = '.'
        decimalFormatSymbols.decimalSeparator = '.'

        val decimalFormat = DecimalFormat("#,###", decimalFormatSymbols)
        return decimalFormat.format(amount) + "đ"
    }

    fun timeIntToDateString(time: Int): String {
        try {
            val simpleDateFormat = SimpleDateFormat(" HH:mm:ss, dd MMMM yyyy", Locale.ENGLISH)
            return simpleDateFormat.format(time * 1000L)

        } catch (e: Exception) {
            return ""
        }
    }
}