package ru.createtogether.bottom_calendar.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.createtogether.bottom_calendar.data.ImageCalendarRepositoryImpl
import ru.createtogether.bottom_calendar.domain.ImageCalendarRepository
import ru.createtogether.feature_network_impl.domain.ErrorHandlerRepository

@Module
@InstallIn(ViewModelComponent::class)
object CalendarModule {

    @Provides
    fun provideHolidayRepository(
        errorHandlerRepository: ErrorHandlerRepository
    ): ImageCalendarRepository {
        return ImageCalendarRepositoryImpl(errorHandlerRepository)
    }
}