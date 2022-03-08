package ru.createtogether.feature_holiday_impl.data

interface HolidayLocalDataSource {
    fun getFavorites(): Array<Int>
    fun addFavorite(holidayId: Int)
    fun isFavorite(holidayId: Int): Boolean
    fun removeFavorite(holidayId: Int)

    var nextDateWithHolidays: String?
    var isNotifyAboutHolidays: Boolean
}