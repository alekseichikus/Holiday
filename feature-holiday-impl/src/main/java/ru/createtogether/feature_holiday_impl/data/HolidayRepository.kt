package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.Flow
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_utils.model.HolidayModel

interface HolidayRepository {
    suspend fun loadHolidays(date: String): Flow<Event<List<HolidayModel>>>
    suspend fun loadHolidaysById(holidays: Array<Int>): Flow<Event<List<HolidayModel>>>
    suspend fun loadNextDateWithHolidays(date: String): Flow<Event<DayModel>>
    suspend fun loadHolidaysOfMonth(date: String): Flow<Event<List<DayModel>>>

    fun getFavorites(): Array<Int>
    fun addFavorite(holiday: Int)
    fun isFavorite(holiday: Int): Boolean
    fun removeFavorite(holiday: Int)

    var nextDateWithHolidays: String?
    var isNotifyAboutHolidays: Boolean
}