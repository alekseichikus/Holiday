package ru.createtogether.feature_cache_impl.domain

interface PreferenceStorage {
    fun getHolidayLikes(): Array<Int>
    fun addHolidayLikes(holiday: Int)
    fun isHolidayLike(holiday: Int): Boolean
    fun removeHolidayLikes(holiday: Int)

    var versionCode: String?
    var nextDayWithHolidays: String?
}