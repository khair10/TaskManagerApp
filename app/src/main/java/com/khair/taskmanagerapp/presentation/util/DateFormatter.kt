package com.khair.taskmanagerapp.presentation.util

import com.khair.taskmanagerapp.dateFormatPattern
import com.khair.taskmanagerapp.timeFormatPattern
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    private val formatTime = SimpleDateFormat(timeFormatPattern)
    private val formatDate = SimpleDateFormat(dateFormatPattern)
    private val calendar = Calendar.getInstance()

    fun timeRangeFormat(timeInSec: Long): String{
        calendar.apply { timeInMillis = timeInSec * 1000 }
        return formatTime.format(calendar.time) + " - " +
                formatTime.format(calendar.apply {
                    timeInMillis += 3600 * 1000
                }.time)
    }

    fun dateFormat(timeInSec: Long): String{
        calendar.apply { timeInMillis = timeInSec * 1000 }
        return formatTime.format(calendar.time) + " - " +
                formatTime.format(calendar.apply { timeInMillis += 3600 * 1000 }.time) + ", " +
                formatDate.format(calendar.apply {
                    timeInMillis = timeInSec * 1000
                }.time)
    }
}