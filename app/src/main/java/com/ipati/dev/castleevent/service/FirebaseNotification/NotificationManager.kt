package com.ipati.dev.castleevent.service.FirebaseNotification

import android.app.*
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.R

class NotificationManager(context: Context) {
    private var mContext: Context = context
    private var mNotificationId: Int = 0
    private var mChannelGroupId: String = "Castle_Event"
    private var mChannelGroupName: String = "FCM"
    private var mChannelId: String = "Castle_FCM"
    private var mChannelName: String = "CastleEvent Notification"
    private lateinit var mNotificationChannelGroup: NotificationChannelGroup
    private lateinit var mNotificationChannel: NotificationChannel
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var mNotification: NotificationCompat.Builder

    private lateinit var mNotificationCompat: NotificationCompat.Builder
    private lateinit var mIntent: Intent
    private lateinit var mIntentPadding: PendingIntent

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationManager(mRemoteMessage: RemoteMessage.Notification, iconLarge: Bitmap?, badgeCount: Int) {
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.createNotificationChannelGroup(createNotificationGroup())
        mNotificationManager.createNotificationChannel(createNotificationChannel())

        mNotification = NotificationCompat.Builder(mContext, mChannelId)
                .setSmallIcon(R.mipmap.event_logo)
                .setAutoCancel(true)
                .setChannelId(mChannelId)
                .setContentTitle(mRemoteMessage.title)
                .setContentText(mRemoteMessage.body)
                .setNumber(badgeCount)


        iconLarge.let {
            mNotification.setStyle(NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(iconLarge)
                    .setSummaryText(mRemoteMessage.body))
        }

        mIntent = Intent(mContext, ListEventActivity::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mIntentPadding = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_ONE_SHOT)

        mNotificationManager.notify(mNotificationId, mNotification.build())
    }

    fun createNotificationLess(mRemoteMessage: RemoteMessage.Notification, iconLarge: Bitmap?) {
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationCompat = NotificationCompat.Builder(mContext, mChannelId)
                .setAutoCancel(true)
                .setContentTitle(mRemoteMessage.title)
                .setContentText(mRemoteMessage.body)
                .setSmallIcon(R.mipmap.event_logo)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        iconLarge?.let {
            mNotificationCompat.setStyle(NotificationCompat.BigPictureStyle().bigPicture(iconLarge).setSummaryText(mRemoteMessage.body))
        }

        mIntent = Intent(mContext, ListEventActivity::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mIntentPadding = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_ONE_SHOT)

        mNotificationManager.notify(mNotificationId, mNotificationCompat.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationGroup(): NotificationChannelGroup {
        mNotificationChannelGroup = NotificationChannelGroup(mChannelGroupId, mChannelGroupName)
        return mNotificationChannelGroup
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val mImportance: Int = NotificationManager.IMPORTANCE_HIGH
        mNotificationChannel = NotificationChannel(mChannelId, mChannelName, mImportance)
        mNotificationChannel.setShowBadge(true)
        mNotificationChannel.enableLights(true)
        mNotificationChannel.group = mChannelGroupId

        return mNotificationChannel
    }


}