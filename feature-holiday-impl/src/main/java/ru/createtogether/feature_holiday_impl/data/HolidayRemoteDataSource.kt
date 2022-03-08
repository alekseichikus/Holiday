package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.Flow
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_utils.model.HolidayByIdsRequest
import ru.createtogether.feature_holiday_utils.model.HolidayModel

interface HolidayRemoteDataSource {
    suspend fun loadHolidays(date: String): Flow<List<HolidayModel>>
    suspend fun loadHolidaysByIds(holidaysId: List<Int>): Flow<List<HolidayModel>>
    suspend fun loadNextDayWithHolidays(date: String): Flow<DayModel>
    suspend fun loadHolidaysOfMonth(date: String): Flow<List<DayModel>>
}