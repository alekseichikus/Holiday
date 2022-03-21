package ru.createtogether.feature_holiday_utils.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class HolidaysByIdRequest(
    val holidays: List<Int>
): Serializable