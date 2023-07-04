package com.npsdk.jetpack_sdk.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.npsdk.module.NPayLibrary
import com.npsdk.module.utils.Constants
import com.npsdk.module.utils.Preference
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


object AppUtils {

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

    fun isHavePublicKey(): String {
        return Preference.getString(
            NPayLibrary.getInstance().activity,
            NPayLibrary.getInstance().sdkConfig.env + Constants.PUBLIC_KEY, ""
        )
    }

    fun isNeedUpdateWebview(context: Context): Boolean {
        val versionCodeWebview = getVersionCode(context, Constants.PACKAGE_WEBVIEW)
        return versionCodeWebview < Constants.MIN_VERSION_WEBVIEW
    }

    private fun getVersionCode(context: Context, packageName: String): Int {
        return try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }

    fun openPlayStore(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${Constants.PACKAGE_WEBVIEW}"))
            intent.setPackage("com.android.vending") // Đảm bảo mở ứng dụng Play Store
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            // Xảy ra khi không tìm thấy ứng dụng Play Store trên thiết bị
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${Constants.PACKAGE_WEBVIEW}")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}