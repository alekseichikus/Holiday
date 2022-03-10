package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.flow
import retrofit2.Response
import ru.createtogether.common.helpers.extension.isNotNull
import ru.createtogether.common.helpers.extension.responseProcessing
import ru.createtogether.feature_holiday_api.api.HolidayApi
import ru.createtogether.feature_holiday_utils.model.HolidaysByIdRequest
import javax.inject.Inject

class HolidayRemoteDataSourceImpl @Inject constructor(private val holidayApi: HolidayApi) :
    HolidayRemoteDataSource {
    override suspend fun loadHolidays(date: String) = flow {
        emit(holidayApi.loadHolidays(date = date).responseProcessing())
    }

    override suspend fun loadHolidaysById(holidaysId: Array<Int>) = flow {
        val holidayByIdsRequest = HolidaysByIdRequest(holidaysId.toList())
        emit(
            holidayApi.loadHolidaysByIds(holidaysByIdRequest = holidayByIdsRequest)
                .responseProcessing()
        )
    }

    override suspend fun loadNextDateWithHolidays(date: String) = flow {
        emit(holidayApi.loadNextDateWithHolidays(date = date).responseProcessing())
    }

    override suspend fun loadHolidaysOfMonth(date: String) = flow {
        emit(holidayApi.loadHolidaysOfMonth(date = date).responseProcessing())
    }
}