package ru.createtogether.feature_holiday_api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import ru.createtogether.feature_holiday_api.api.HolidayApi

@Module
@InstallIn(ViewModelComponent::class)
object HolidayApiModule {
    @Provides
    fun provideHolidayApi(retrofit: Retrofit): HolidayApi {
        return retrofit.create(HolidayApi::class.java)
    }
}