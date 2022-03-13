package ru.createtogether.feature_holiday_impl.data

import ru.createtogether.feature_country_utils.helpers.CountryEnum
import ru.createtogether.feature_holiday_utils.helpers.HolidayTypeEnum
import ru.createtogether.feature_holiday_utils.model.HolidayModel

object HolidayData {
    val holiday0 = HolidayModel(
        1,
        HolidayTypeEnum.DEFAULT,
        "0 праздник",
        false,
        "Описание праздника",
        "10-12-2021",
        null,
        CountryEnum.INTERNATIONAL
    )

    val holiday1 = HolidayModel(
        2,
        HolidayTypeEnum.DEFAULT,
        "1 праздник",
        false,
        "Описание праздника",
        "10-12-2021",
        null,
        CountryEnum.INTERNATIONAL
    )

    val holiday2 = HolidayModel(
        3,
        HolidayTypeEnum.DEFAULT,
        "2 праздник",
        false,
        "Описание праздника",
        "10-12-2021",
        null,
        CountryEnum.INTERNATIONAL
    )

    val holiday3 = HolidayModel(
        4,
        HolidayTypeEnum.DEFAULT,
        "3 праздник",
        false,
        "Описание праздника",
        "11-12-2021",
        null,
        CountryEnum.INTERNATIONAL
    )

    val holiday4 = HolidayModel(
        5,
        HolidayTypeEnum.DEFAULT,
        "3 праздник",
        false,
        "Описание праздника",
        "11-12-2021",
        null,
        CountryEnum.INTERNATIONAL
    )

    fun getHolidays() = mutableListOf(holiday0, holiday1, holiday2, holiday3, holiday4)

    //remote
    fun getFavorites() = arrayOf(1, 2, 5)
}