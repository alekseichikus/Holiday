package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.Flow
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_utils.model.HolidayModel

interface HolidayRemoteDataSource {
    suspend fun loadHolidays(date: String): Flow<List<HolidayModel>>
    suspend fun loadHolidaysById(holidaysId: Array<Int>): Flow<List<HolidayModel>>
    suspend fun loadNextDateWithHolidays(date: String): Flow<DayModel>
    suspend fun loadHolidaysOfMonth(date: String): Flow<List<DayModel>>
}