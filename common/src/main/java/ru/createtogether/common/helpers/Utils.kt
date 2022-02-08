package ru.createtogether.common.helpers

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun convertDateStringToLong(date: String) = SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN, Locale.getDefault()).parse(date).time

    fun convertDateToDateString(date: Date) = SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN, Locale.getDefault()).format(date)
}