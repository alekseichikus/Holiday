package ru.createtogether.feature_notification_impl.domain

import android.content.Context
import ru.createtogether.feature_notification_impl.helpers.ReasonNotification

interface BasePushNotificationRepository {
    fun createPush(context: Context, data: Map<String, String>)

    fun getData(title: String, body: String, image: String? = null, addData: Map<String, String> = mapOf(), reasonNotification: ReasonNotification): Map<String, String>
}