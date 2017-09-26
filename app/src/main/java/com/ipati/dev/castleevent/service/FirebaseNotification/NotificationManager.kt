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
    var mContext: Context = context
    var mNotificationId: Int = 0
    var mChannelGroupId: String = "Castle_Event"
    var mChannelGroupName: String = "FCM"
    var mChannelId: String = "Castle_FCM"
    var mChannelName: String = "CastleEvent Notification"
    lateinit var mNotificationChannelGroup: NotificationChannelGroup
    lateinit var mNotificationChannel: NotificationChannel
    lateinit var mNotificationManager: NotificationManager
    lateinit var mNotification: Notification

    lateinit var mNotificationCompat: NotificationCompat.Builder
    lateinit var mIntent: Intent
    lateinit var mIntentPadding: PendingIntent


    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationManager(mRemoteMessage: RemoteMessage.Notification, iconLarge: Bitmap, badgeCount: Int) {
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.createNotificationChannelGroup(createNotificationGroup())
        mNotificationManager.createNotificationChannel(createNotificationChannel())

        mNotification = NotificationCompat.Builder(mContext, mChannelId)
                .setSmallIcon(R.mipmap.event_logo)
                .setAutoCancel(true)
                .setChannelId(mChannelId)
                .setContentTitle(mRemoteMessage.title)
                .setContentText(mRemoteMessage.body)
                .setLargeIcon(iconLarge)
                .setNumber(badgeCount)
                .build()

        mIntent = Intent(mContext, ListEventActivity::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mIntentPadding = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_ONE_SHOT)

        mNotificationManager.notify(mNotificationId, mNotification)
    }

    fun createNotificationLess(mRemoteMessage: RemoteMessage.Notification, iconLarge: Bitmap) {
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationCompat = NotificationCompat.Builder(mContext, mChannelId)
                .setAutoCancel(true)
                .setContentTitle(mRemoteMessage.title)
                .setContentText(mRemoteMessage.body)
                .setLargeIcon(iconLarge)
                .setSmallIcon(R.mipmap.event_logo)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

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