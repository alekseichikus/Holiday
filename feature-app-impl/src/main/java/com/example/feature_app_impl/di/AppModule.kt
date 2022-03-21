package com.example.feature_app_impl.di

import com.example.feature_app_impl.data.AppLocalDataSource
import com.example.feature_app_impl.data.AppLocalDataSourceImpl
import com.example.feature_app_impl.data.AppRepository
import com.example.feature_app_impl.data.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository

    @Binds
    abstract fun provideAppLocalRepository(appLocalDataSourceImpl: AppLocalDataSourceImpl): AppLocalDataSource
}