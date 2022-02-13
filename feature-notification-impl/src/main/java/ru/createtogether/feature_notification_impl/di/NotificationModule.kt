package ru.createtogether.feature_notification_impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import ru.createtogether.feature_notification_impl.data.BasePushNotificationRepositoryImpl
import ru.createtogether.feature_notification_impl.domain.BasePushNotificationRepository

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    fun provideBaseNotificationRepository(): BasePushNotificationRepository {
        return BasePushNotificationRepositoryImpl()
    }
}