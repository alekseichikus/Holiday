package ru.createtogether.feature_notification_impl.data

import android.content.Context
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

                        NotificationHelper(context).createInfoNotification(
                            title = title,
                            body = body
                        )
                    }
                }
            }
        }
    }

    override fun getData(
        title: String,
        body: String,
        addData: Map<String, String>,
        reasonNotification: ReasonNotification
    ): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put(TITLE, title)
            put(BODY, body)
            put(REASON, reasonNotification.toString())
            putAll(addData)
        }
    }

    companion object {
        private const val REASON = "reason"
        private const val TITLE = "title"
        private const val BODY = "body"
    }
}