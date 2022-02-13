package ru.createtogether.feature_worker_impl.di

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
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

        val refreshCpnWork = OneTimeWorkRequest.Builder(HolidayWorker::class.java)
            .setInitialDelay(3, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .addTag(HolidayWorker.TAG)
            .build()
        WorkManager.getInstance(context).enqueue(refreshCpnWork)
    }
}