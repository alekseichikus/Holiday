package ru.createtogether.feature_notification_impl.helpers

import android.app.*
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*
import ru.createtogether.feature_notification_impl.R

class NotificationHelper(private val context: Context) {

	@RequiresApi(Build.VERSION_CODES.O)
	private fun createNotificationChannel(
		groupId: String,
		groupName: String,
		channelId: String,
		channelName: String,
	) {
		(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))

		val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
		notificationChannel.group = groupId
		(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(notificationChannel)
	}

	fun createInfoNotification(title: String, body: String) {

		val eventMessages  = context.getString(R.string.group_id_holidays)
		val eventChannel = context.getString(R.string.channel_events)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createNotificationChannel(
				GROUP_ID_HOLIDAYS,
				eventMessages,
				CHANNEL_DEFAULT,
				eventChannel
			)
		}

		NotificationManagerCompat.from(context).notify(Random().nextInt(),
			getNotification(title = title, body = body))
	}

	private fun getNotification(title: String, body: String): Notification {
		return NotificationCompat.Builder(context, CHANNEL_DEFAULT)
			.setSmallIcon(R.drawable.ic_icon_app)
			.setContentTitle(title)
			.setContentText(body)
			.setAutoCancel(true)
			.build()
	}

	companion object {
		private const val CHANNEL_DEFAULT= "CHANNEL_DEFAULT"

		private const val GROUP_ID_HOLIDAYS = "GROUP_ID_HOLIDAYS"
	}
}
