package ru.createtogether.feature_holiday_impl.di

import dagger.Binds
import dagger.Module
import ru.createtogether.feature_holiday_impl.data.*

@Module()
interface HolidayModule {
    @Binds
    fun provideHolidayRepository(holidayRepositoryImpl: HolidayRepositoryImpl): HolidayRepository

    @Binds
    fun provideHolidayLocalRepository(holidayLocalDataSourceImpl: HolidayLocalDataSourceImpl): HolidayLocalDataSource

    @Binds
    fun provideHolidayRemoteRepository(holidayRemoteDataSourceImpl: HolidayRemoteDataSourceImpl): HolidayRemoteDataSource
}