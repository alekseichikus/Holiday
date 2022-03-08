package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ru.createtogether.common.helpers.Event
import javax.inject.Inject

class HolidayRepositoryImpl @Inject constructor(
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

    override suspend fun loadHolidaysById(holidays: Array<Int>) = flow {
        holidayRemoteDataSource.loadHolidaysById(holidaysId = holidays).collect { holidays ->
            holidays.forEach {
                it.isLike = holidayCacheRepository.isFavorite(it.id)
            }
            emit(Event.success(holidays))
        }
    }

    override suspend fun loadNextDateWithHolidays(date: String) = flow {
        holidayRemoteDataSource.loadNextDateWithHolidays(date = date).collect { day ->
            emit(Event.success(day))
        }
    }

    override suspend fun loadHolidaysOfMonth(date: String) = flow {
        holidayRemoteDataSource.loadHolidaysOfMonth(date = date).collect { days ->
            emit(Event.success(days))
        }
    }

    override fun getFavorites() = holidayCacheRepository.getFavorites()

    override fun addFavorite(holiday: Int) {
        holidayCacheRepository.addFavorite(holidayId = holiday)
    }

    override fun isFavorite(holiday: Int) = holidayCacheRepository.isFavorite(holiday)

    override fun removeFavorite(holiday: Int) = holidayCacheRepository.removeFavorite(holiday)

    override var nextDateWithHolidays = holidayCacheRepository.nextDateWithHolidays

    override var isNotifyAboutHolidays = holidayCacheRepository.isNotifyAboutHolidays
}