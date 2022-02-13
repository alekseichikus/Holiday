package ru.createtogether.feature_characteristic_utils

import androidx.annotation.DrawableRes
import java.io.Serializable

data class CharacteristicModel(
    @DrawableRes
    val imageRes: Int,
    var text: String,
    var description: String,
): Serializable