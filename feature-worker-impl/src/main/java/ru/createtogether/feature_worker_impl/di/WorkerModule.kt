package ru.createtogether.feature_worker_impl.di

import android.content.Context
import androidx.work.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import ru.createtogether.feature_notification_impl.data.BasePushNotificationRepositoryImpl
import ru.createtogether.feature_notification_impl.domain.BasePushNotificationRepository
import ru.createtogether.feature_worker_impl.HolidayWorker
import java.util.concurrent.TimeUnit

object WorkerModule {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun runHolidayWorker(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(HolidayWorker.TAG)

        val refreshCpnWork = PeriodicWorkRequest.Builder(HolidayWorker::class.java, 1, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(HolidayWorker.TAG)
            .build()
        WorkManager.getInstance(context).enqueue(refreshCpnWork)
    }
}