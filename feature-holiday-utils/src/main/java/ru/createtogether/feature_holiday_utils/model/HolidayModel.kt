package ru.createtogether.feature_holiday_utils.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.createtogether.feature_country_utils.helpers.CountryEnum
import ru.createtogether.feature_holiday_utils.helpers.HolidayTypeEnum
import ru.createtogether.feature_photo_utils.PhotoModel
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class HolidayModel(
    val id: Int,
    var type: HolidayTypeEnum?,
    @Json(name = "name")
    val title: String,
    @Json(ignore = true)
    var isLike: Boolean = false,
    val description: String,
    var date: String,
    @Json(name = "urls")
    val images: List<PhotoModel>?,
    val region: CountryEnum
): Serializable