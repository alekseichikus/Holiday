package ru.createtogether.feature_holiday_utils.model

import java.io.Serializable

data class HolidaysByIdRequest(
    val holidays: List<Int>
): Serializable