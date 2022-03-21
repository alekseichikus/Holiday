package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.Flow
import ru.createtogether.feature_day_utils.model.DayOfMonthModel
import ru.createtogether.feature_day_utils.model.NextDayModel
import ru.createtogether.feature_holiday_utils.model.HolidayModel

interface HolidayRepository {
    suspend fun loadHolidays(date: String): Flow<List<HolidayModel>>
    suspend fun loadHolidaysById(holidaysId: Array<Int>): Flow<List<HolidayModel>>
    suspend fun loadNextDateWithHolidays(date: String): Flow<NextDayModel>
    suspend fun loadHolidaysOfMonth(date: String): Flow<List<DayOfMonthModel>>

    fun getFavorites(): Array<Int>
    fun addFavorite(holiday: Int)
    fun isFavorite(holiday: Int): Boolean
    fun removeFavorite(holiday: Int)

    var nextDateWithHolidays: String?
    var isNotifyAboutHolidays: Boolean
}