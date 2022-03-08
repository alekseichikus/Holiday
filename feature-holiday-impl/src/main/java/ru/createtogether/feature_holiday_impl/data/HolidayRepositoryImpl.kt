package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.createtogether.common.helpers.Event
import ru.createtogether.feature_network_impl.domain.ErrorHandlerRepository
import ru.createtogether.feature_holiday_api.api.HolidayApi
import javax.inject.Inject

class HolidayRepositoryImpl @Inject constructor(
    private val holidayApi: HolidayApi,
    private val errorHandlerRepository: ErrorHandlerRepository,
    private val holidayCacheRepository: HolidayLocalDataSource,
    private val holidayRemoteDataSource: HolidayRemoteDataSource
) : HolidayRepository {
    override suspend fun loadHolidays(date: String) = flow {
        holidayRemoteDataSource.loadHolidays(date = date).collect { holidays ->
            holidays.forEach {
                it.isLike = holidayCacheRepository.isFavorite(it.id)
            }
            emit(Event.success(holidays))
        }
    }

    override suspend fun loadHolidaysByIds(holidays: Array<Int>) = flow {
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

    override suspend fun loadNextDayWithHolidays(date: String) = flow {
        emit(Event.loading())

        val apiResponse = holidayApi.loadNextDateWithHolidays(date)
        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            emit(Event.success(apiResponse.body()))
            return@flow
        }
        val errorMessage = withContext(Dispatchers.IO) { apiResponse.errorBody()?.string() }
        emit(Event.error(errorHandlerRepository.handleErrorMessage(errorMessage)))
    }.catch { e ->
        emit(Event.error(errorHandlerRepository.handleErrorResponse(e)))
    }

    override suspend fun loadHolidaysOfMonth(date: String) = flow {
        emit(Event.loading())

        val apiResponse = holidayApi.loadHolidaysOfMonth(date)
        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            emit(Event.success(apiResponse.body()))
            return@flow
        }
        val errorMessage = withContext(Dispatchers.IO) { apiResponse.errorBody()?.string() }
        emit(Event.error(errorHandlerRepository.handleErrorMessage(errorMessage)))
    }.catch { e ->
        emit(Event.error(errorHandlerRepository.handleErrorResponse(e)))
    }

    override fun getFavorites() = holidayCacheRepository.getFavorites()

    override fun addFavorite(holiday: Int) {
        holidayCacheRepository.addFavorite(holiday = holiday)
    }

    override fun isFavorite(holiday: Int) = holidayCacheRepository.isFavorite(holiday)

    override fun removeFavorite(holiday: Int) = holidayCacheRepository.removeFavorite(holiday)

    override var nextDateWithHolidays = holidayCacheRepository.nextDateWithHolidays

    override var isNotifyAboutHolidays = holidayCacheRepository.isNotifyAboutHolidays
}