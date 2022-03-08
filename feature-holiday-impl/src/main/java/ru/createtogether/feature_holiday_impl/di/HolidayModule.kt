package ru.createtogether.feature_holiday_impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.createtogether.feature_holiday_impl.data.*

@Module
@InstallIn(SingletonComponent::class)
abstract class HolidayModule {
    @Binds
    abstract fun provideHolidayRepository(holidayRepositoryImpl: HolidayRepositoryImpl): HolidayRepository

    @Binds
    abstract fun provideHolidayLocalRepository(holidayLocalDataSourceImpl: HolidayLocalDataSourceImpl): HolidayLocalDataSource

    @Binds
    abstract fun provideHolidayRemoteRepository(holidayRemoteDataSourceImpl: HolidayRemoteDataSourceImpl): HolidayRemoteDataSource
}