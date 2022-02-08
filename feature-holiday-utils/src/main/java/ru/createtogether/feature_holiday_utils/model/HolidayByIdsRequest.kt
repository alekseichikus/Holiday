package ru.createtogether.feature_holiday_utils.model

import java.io.Serializable

data class HolidayByIdsRequest(
    val holidays: List<Int>
): Serializable