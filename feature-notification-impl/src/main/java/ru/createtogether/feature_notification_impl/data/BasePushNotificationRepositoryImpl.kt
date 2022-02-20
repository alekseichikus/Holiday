package ru.createtogether.feature_notification_impl.data

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.createtogether.feature_notification_impl.domain.BasePushNotificationRepository
import ru.createtogether.feature_notification_impl.helpers.NotificationHelper
import ru.createtogether.feature_notification_impl.helpers.ReasonNotification
class BasePushNotificationRepositoryImpl : BasePushNotificationRepository {
    override fun createPush(context: Context, data: Map<String, String>) {
        with(data) {
            get(REASON)?.let {
                when (ReasonNotification.valueOf(it)) {
                    ReasonNotification.HOLIDAY -> {
                        val title = get(TITLE) ?: String()
                        val body = get(BODY) ?: String()
                        val image = get(IMAGE)

                        val intent = Intent()
                        intent.setClassName(context, "ru.createtogether.holiday.MainActivity")
                        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        NotificationHelper(context).createInfoNotification(
                            title = title,
                            body = body,
                            image = image,
                            pendingIntent = pendingIntent
                        )
                    }
                }
            }
        }
    }

    override fun getData(
        title: String,
        body: String,
        image: String?,
        addData: Map<String, String>,
        reasonNotification: ReasonNotification
    ): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put(TITLE, title)
            put(BODY, body)
            image?.let {
                put(IMAGE, it)
            }
            put(REASON, reasonNotification.toString())
            putAll(addData)
        }
    }

    companion object {
        private const val REASON = "reason"
        private const val TITLE = "title"
        private const val BODY = "body"
        private const val IMAGE = "image"
    }
}