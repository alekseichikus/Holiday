package ru.createtogether.feature_day_utils.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class DayOfMonthModel(
    @Json(name = "holidayId")
    var id: Int,
    var dateString: String,
    var holidaysCount: Int
): Serializable