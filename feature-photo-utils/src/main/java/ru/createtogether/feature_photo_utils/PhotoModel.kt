package ru.createtogether.feature_photo_utils

import java.io.Serializable

data class PhotoModel(val id: Int, val url: String, var isSelected: Boolean = false) : Serializable