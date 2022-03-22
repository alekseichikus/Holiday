package ru.createtogether.feature_holiday_impl.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import javax.inject.Inject

class HolidayRepositoryImpl @Inject constructor(
    private val holidayLocalRepository: HolidayLocalDataSource,
    private val holidayRemoteDataSource: HolidayRemoteDataSource
) : HolidayRepository {
    override var nextDateWithHolidays = holidayLocalRepository.nextDateWithHolidays

    override var isNotifyAboutHolidays = holidayLocalRepository.isNotifyAboutHolidays

    override suspend fun loadHolidays(date: String) = flow {
        holidayRemoteDataSource.loadHolidays(date = date).collect { holidays ->
            holidays.forEach {
                it.isLike = holidayLocalRepository.isFavorite(it.id)
            }
            emit(holidays)
        }
    }

    override suspend fun loadHolidaysById(holidaysId: Array<Int>) = flow {
        holidayRemoteDataSource.loadHolidaysById(holidaysId = holidaysId).collect { holidays ->
            holidays.forEach {
                it.isLike = holidayLocalRepository.isFavorite(it.id)
            }
            emit(holidays)
        }
    }

    override suspend fun loadNextDateWithHolidays(date: String) = flow {
        holidayRemoteDataSource.loadNextDateWithHolidays(date = date).collect { day ->
            emit(day)
        }
    }

    override suspend fun loadHolidaysOfMonth(date: String) = flow {
        holidayRemoteDataSource.loadHolidaysOfMonth(date = date).collect { days ->
            emit(days)
        }
    }

    override fun getFavorites() = holidayLocalRepository.getFavorites()

    override fun addFavorite(holiday: Int) {
        holidayLocalRepository.addFavorite(holidayId = holiday)
    }

    override fun isFavorite(holiday: Int) = holidayLocalRepository.isFavorite(holiday)

    override fun removeFavorite(holiday: Int) = holidayLocalRepository.removeFavorite(holiday)
}