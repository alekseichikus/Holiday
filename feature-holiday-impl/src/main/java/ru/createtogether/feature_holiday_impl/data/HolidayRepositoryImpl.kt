package ru.createtogether.feature_holiday_impl.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_holiday_impl.domain.HolidayRepository
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_network_impl.domain.ErrorHandlerRepository
import ru.createtogether.feature_holiday_api.api.HolidayApi
import javax.inject.Inject

class HolidayRepositoryImpl @Inject constructor(
    private val holidayApi: HolidayApi,
    private val errorHandlerRepository: ErrorHandlerRepository
) : HolidayRepository {
    override fun loadHolidays(date: String) = flow<Event<List<HolidayModel>>> {
        emit(Event.loading())

        val apiResponse = holidayApi.loadHolidays(date)
        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            emit(Event.success(apiResponse.body()?.sortedByDescending { it.description }))
            return@flow
        }
        val errorMessage = withContext(Dispatchers.IO) { apiResponse.errorBody()?.string() }
        emit(Event.error(errorHandlerRepository.handleErrorMessage(errorMessage)))
    }.catch { e ->
        emit(Event.error(errorHandlerRepository.handleErrorResponse(e)))
    }

    override fun loadHolidaysByIds(holidays: Array<Int>) = flow {
        emit(Event.loading())

        val apiResponse = holidayApi.loadHolidaysByIds(
            ru.createtogether.feature_holiday_utils.model.HolidayByIdsRequest(
                holidays = holidays.toList()
            )
        )
        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            emit(Event.success(apiResponse.body()?.sortedBy { it.title }?.sortedBy { it.date }))
            return@flow
        }

        val errorMessage = withContext(Dispatchers.IO) { apiResponse.errorBody()?.string() }
        emit(Event.error(errorHandlerRepository.handleErrorMessage(errorMessage)))
    }.catch { e ->
        emit(Event.error(errorHandlerRepository.handleErrorResponse(e)))
    }

    override fun loadNextDayWithHolidays(date: String) = flow {
        emit(Event.loading())

        val apiResponse = holidayApi.loadNextDayWithHolidays(date)
        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            emit(Event.success(apiResponse.body()))
            return@flow
        }
        val errorMessage = withContext(Dispatchers.IO) { apiResponse.errorBody()?.string() }
        emit(Event.error(errorHandlerRepository.handleErrorMessage(errorMessage)))
    }.catch { e ->
        emit(Event.error(errorHandlerRepository.handleErrorResponse(e)))
    }
}