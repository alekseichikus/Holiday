package ru.createtogether.feature_holiday_utils.helpers

import ru.createtogether.feature_holiday_utils.R

object HolidayUtil {
    fun getTypeName(holidayTypeEnum: HolidayTypeEnum?): Int {
        return when (holidayTypeEnum) {
            HolidayTypeEnum.RELIGION -> R.string.category_type_religion
            HolidayTypeEnum.FAMILY -> R.string.category_type_family
            else -> R.string.category_type_another
        }
    }
}