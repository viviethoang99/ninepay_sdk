package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dt.composedatepicker.ComposeCalendar
import com.dt.composedatepicker.SelectDateListener
import java.util.*
import com.npsdk.R
import com.npsdk.jetpack_sdk.theme.initColor

@Composable
fun DatePicker(onDateSelected: (month: Int, year: Int) -> Unit, onCancel: () -> Unit) {

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, 2000)
    calendar.set(Calendar.MONTH, 0)
    val calendarMax = Calendar.getInstance()
    calendarMax.set(Calendar.YEAR, 2070)
    calendarMax.set(Calendar.MONTH, 12)

    Box(
        Modifier
            .fillMaxWidth()
            .height(470.dp)
            .background(color = Color.Transparent), contentAlignment = Alignment.Center
    ) {

        ComposeCalendar(minDate = calendar.time,
            maxDate = calendarMax.time,
            locale = Locale("vi"),
            title = "Chọn ngày hiệu lực/hết hạn",
            negativeButtonTitle = "Hủy bỏ",
            positiveButtonTitle = "OK",
            themeColor = initColor(),
            listener = object : SelectDateListener {
                override fun onDateSelected(date: Date) {
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.time = date
                    val month: Int = calendar.get(Calendar.MONTH) + 1
                    val year: Int = calendar.get(Calendar.YEAR)
                    onDateSelected(month, year)
                }

                override fun onCanceled() {
                    onCancel()
                }
            })
    }
}
