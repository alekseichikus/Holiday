package ru.createtogether.feature_holiday_impl.data.data

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import ru.createtogether.feature_holiday_impl.data.HolidayData
import ru.createtogether.feature_holiday_impl.data.HolidayRepositoryImpl

class HolidayRepositoryTest {

    private lateinit var holidayRemoteDataSource: FakeHolidayRemoteDataSource
    private lateinit var holidayLocalDataSource: FakeHolidayLocalDataSource

    private lateinit var holidayRepository: HolidayRepositoryImpl

    @Before
    fun createRepository() {
        holidayRemoteDataSource = FakeHolidayRemoteDataSource(HolidayData.getHolidays())
        holidayLocalDataSource = FakeHolidayLocalDataSource(HolidayData.getHolidays())

        holidayRepository = HolidayRepositoryImpl(holidayLocalDataSource, holidayRemoteDataSource)
    }

    @Test
    fun getHolidays_requestsAllHolidaysFromRemoteDataSource() = runBlocking {
        val holidays = holidayRepository.loadHolidays("10-12-2021")

        holidays.collect { holidays ->
            assertThat(holidays.filter { it.isLike }.size, `is`(2))
        }
    }

    @Test
    fun getHolidays_requestsHolidaysByIdFromRemoteDataSource() = runBlocking {
        val holidays = holidayRepository.loadHolidaysById(HolidayData.getFavorites())

        holidays.collect { holidays ->
            assertThat(holidays.filter { it.isLike }.size, `is`(HolidayData.getFavorites().size))
        }
    }
}