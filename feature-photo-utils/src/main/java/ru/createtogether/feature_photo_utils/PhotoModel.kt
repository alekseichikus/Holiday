package ru.createtogether.feature_photo_utils

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class PhotoModel(
    val id: Int,
    val url: String,
    @Json(ignore = true)
    var isSelected: Boolean = false
) : Serializable