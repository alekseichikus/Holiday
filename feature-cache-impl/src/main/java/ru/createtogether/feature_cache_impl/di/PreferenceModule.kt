package ru.createtogether.feature_cache_impl.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.createtogether.feature_cache_impl.data.PreferenceStorageImpl
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        PreferenceStorageImpl(context)
}