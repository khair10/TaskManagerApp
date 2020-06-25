package com.khair.taskmanagerapp.presentation.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    private val formatTime = SimpleDateFormat("HH:mm")
    private val formatDate = SimpleDateFormat("HH:mm, d MMMM, yyyy")
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
                formatDate.format(calendar.apply {
                    timeInMillis += 3600 * 1000
                }.time)
    }
}