package ru.createtogether.feature_country_utils.helpers

import ru.createtogether.feature_country_utils.R

object CountryUtil {
    fun getCountryName(country: CountryEnum) = when (country) {
        CountryEnum.RU -> R.string.country_russia
        else -> R.string.international
    }
}