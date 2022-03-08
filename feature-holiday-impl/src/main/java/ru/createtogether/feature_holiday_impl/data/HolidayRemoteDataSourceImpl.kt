package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.flow
import retrofit2.Response
import ru.createtogether.common.helpers.extension.isNotNull
import ru.createtogether.feature_holiday_api.api.HolidayApi
import ru.createtogether.feature_holiday_utils.model.HolidaysByIdRequest
import javax.inject.Inject

class HolidayRemoteDataSourceImpl @Inject constructor(private val holidayApi: HolidayApi) :
    HolidayRemoteDataSource {
    override suspend fun loadHolidays(date: String) = flow {
        emit(responseProcessing(holidayApi.loadHolidays(date = date)))
    }

    override suspend fun loadHolidaysById(holidaysId: Array<Int>) = flow {
        val holidayByIdsRequest = HolidaysByIdRequest(holidaysId.toList())
        emit(responseProcessing(holidayApi.loadHolidaysByIds(holidaysByIdRequest = holidayByIdsRequest)))
    }

    override suspend fun loadNextDateWithHolidays(date: String) = flow {
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