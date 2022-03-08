package ru.createtogether.feature_holiday_impl.data

interface HolidayLocalDataSource {
    fun getFavorites(): Array<Int>
    fun addFavorite(holiday: Int)
    fun isFavorite(holiday: Int): Boolean
    fun removeFavorite(holiday: Int)

    var nextDateWithHolidays: String?
    var isNotifyAboutHolidays: Boolean
}