package ru.createtogether.feature_worker_impl

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Status
import ru.createtogether.feature_cache_impl.domain.PreferenceStorage
import ru.createtogether.feature_holiday_impl.data.HolidayRepositoryImpl
import ru.createtogether.feature_holiday_impl.domain.HolidayRepository
import ru.createtogether.feature_notification_impl.domain.BasePushNotificationRepository
import ru.createtogether.feature_notification_impl.helpers.ReasonNotification
import ru.createtogether.feature_worker_impl.di.WorkerModule
import javax.inject.Inject

@HiltWorker
class HolidayWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    val notificationRepository: BasePushNotificationRepository,
    var holidayRepository: HolidayRepository,
    var preferenceStorage: PreferenceStorage,
) : Worker(context, params) {

    override fun doWork(): Result {
        GlobalScope.launch {
            preferenceStorage.nextDayWithHolidays?.let { date ->
                holidayRepository.loadHolidays(date = date)
                    .collect {
                        when (it.status) {
                            Status.SUCCESS -> {
                                it.data?.first()?.let { holiday ->
                                    notificationRepository.createPush(
                                        context = context,
                                        notificationRepository.getData(
                                            title = "${holiday.date}. ${holiday.title}",
                                            body = holiday.description,
                                            image = holiday.images?.first()?.url,
                                            reasonNotification = ReasonNotification.HOLIDAY
                                        )
                                    )
                                }
                            }
                        }
                    }
            }
        }
        return Result.success()
    }

    companion object {
        const val TAG = "holiday_worker"
        const val EVEN_HOURS = 2L
    }
}