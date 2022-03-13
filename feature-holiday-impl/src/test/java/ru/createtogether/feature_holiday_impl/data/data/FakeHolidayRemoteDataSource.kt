package ru.createtogether.feature_holiday_impl.data.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_holiday_impl.data.HolidayRemoteDataSource
import ru.createtogether.feature_holiday_utils.model.HolidayModel

class FakeHolidayRemoteDataSource(var holidays: MutableList<HolidayModel> = mutableListOf()) :
    HolidayRemoteDataSource {
    override suspend fun loadHolidays(date: String) = flow {
        emit(holidays.filter { it.date == date }.toList())
    }

    override suspend fun loadHolidaysById(holidaysId: Array<Int>) = flow {
        emit(holidays.filter { holidaysId.contains(it.id) })
    }

    override suspend fun loadNextDateWithHolidays(date: String): Flow<DayModel> {
        TODO("Not yet implemented")
    }

    override suspend fun loadHolidaysOfMonth(date: String): Flow<List<DayModel>> {
        TODO("Not yet implemented")
    }
}