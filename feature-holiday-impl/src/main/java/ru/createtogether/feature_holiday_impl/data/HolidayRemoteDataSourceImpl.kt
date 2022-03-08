package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.flow
import retrofit2.Response
import ru.createtogether.common.helpers.extension.isNotNull
import ru.createtogether.feature_holiday_api.api.HolidayApi
import ru.createtogether.feature_holiday_utils.model.HolidayByIdsRequest
import javax.inject.Inject

class HolidayRemoteDataSourceImpl @Inject constructor(private val holidayApi: HolidayApi) :
    HolidayRemoteDataSource {
    override suspend fun loadHolidays(date: String) = flow {
        emit(responseProcessing(holidayApi.loadHolidays(date = date)))
    }

    override suspend fun loadHolidaysByIds(holidaysId: List<Int>) = flow {
        val holidayByIdsRequest = HolidayByIdsRequest(holidaysId)
        emit(responseProcessing(holidayApi.loadHolidaysByIds(holidayByIdsRequest = holidayByIdsRequest)))
    }

    override suspend fun loadNextDayWithHolidays(date: String) = flow {
        emit(responseProcessing(holidayApi.loadNextDateWithHolidays(date = date)))
    }

    override suspend fun loadHolidaysOfMonth(date: String) = flow {
        emit(responseProcessing(holidayApi.loadHolidaysOfMonth(date = date)))
    }

    private fun <T> responseProcessing(response: Response<T>): T {
        with(response) {
            if (isSuccessful && body().isNotNull()) {
                return body()!!
            } else
                throw IllegalArgumentException()
        }
    }
}