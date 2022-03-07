package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.Flow
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_utils.model.HolidayModel

interface HolidayRepository {
    suspend fun loadHolidays(date: String): Flow<Event<List<HolidayModel>>>
    fun loadHolidaysByIds(holidays: Array<Int>): Flow<Event<List<HolidayModel>>>
    fun loadNextDayWithHolidays(date: String): Flow<Event<DayModel>>
    fun loadHolidaysOfMonth(date: String): Flow<Event<List<DayModel>>>
}