package ru.createtogether.feature_holiday_utils.model

import com.google.gson.annotations.SerializedName
import ru.createtogether.feature_country_utils.helpers.CountryEnum
import ru.createtogether.feature_holiday_utils.helpers.HolidayTypeEnum
import ru.createtogether.feature_photo_utils.PhotoModel
import java.io.Serializable

data class HolidayModel(
    val id: Int,
    var type: HolidayTypeEnum?,
    @SerializedName("name")
    val title: String,
    var isLike: Boolean,
    val description: String,
    var date: String,
    @SerializedName("urls")
    val images: List<PhotoModel>?,
    val data: List<Pair<String, String>>?,
    val region: CountryEnum
): Serializable