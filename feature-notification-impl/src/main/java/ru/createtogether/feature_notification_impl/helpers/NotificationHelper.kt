package ru.createtogether.feature_notification_impl.helpers

import android.app.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ru.createtogether.common.helpers.Status
import ru.createtogether.feature_notification_impl.R
import java.util.*

class NotificationHelper(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        groupId: String,
        groupName: String,
        channelId: String,
        channelName: String,
    ) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannelGroup(
            NotificationChannelGroup(groupId, groupName)
        )

        val notificationChannel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.group = groupId
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            notificationChannel
        )
    }

    fun createInfoNotification(
        title: String,
        body: String,
        image: String?,
        pendingIntent: PendingIntent
    ) {

        val eventMessages = context.getString(R.string.group_id_holidays)
        val eventChannel = context.getString(R.string.channel_events)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                GROUP_ID_HOLIDAYS,
                eventMessages,
                CHANNEL_DEFAULT,
                eventChannel
            )
        }
        getNotification(title = title, body = body, image = image, pendingIntent = pendingIntent)
    }

    private fun getNotification(title: String, body: String, image: String?, pendingIntent: PendingIntent) {
        Glide.with(context)
            .asBitmap()
            .load(image)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    NotificationManagerCompat.from(context).notify(
                        Random().nextInt(), NotificationCompat.Builder(context, CHANNEL_DEFAULT)
                            .setSmallIcon(R.drawable.ic_icon_app)
                            .setContentTitle(title)
                            .setContentIntent(pendingIntent)
                            .setStyle(
                                NotificationCompat.BigPictureStyle().bigPicture(resource)
                            )
                            .setContentText(body)
                            .setAutoCancel(true)
                            .build()
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    companion object {
        private const val MODE_SMALL = "MODE_SMALL"

        private const val CHANNEL_DEFAULT = "CHANNEL_DEFAULT"

        private const val GROUP_ID_HOLIDAYS = "GROUP_ID_HOLIDAYS"
    }
}
