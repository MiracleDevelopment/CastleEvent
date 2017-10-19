package com.ipati.dev.castleevent.service.FirebaseNotification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ipati.dev.castleevent.R
import java.net.URL

class FirebaseMessagingService : FirebaseMessagingService() {
    private var mMapData: Map<String, String>? = null
    private var mNotificationManager: NotificationManager = NotificationManager(this)
    lateinit var mRemoteMessageNotification: RemoteMessage.Notification

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        mRemoteMessageNotification = p0?.notification!!
        mMapData = p0.data

        if (Build.VERSION.SDK_INT >= 26) {
            sendNotification(mRemoteMessageNotification)
        } else {
            sendNotificationLess(mRemoteMessageNotification)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(mRemoteMessage: RemoteMessage.Notification) {
        mNotificationManager.createNotificationManager(mRemoteMessage, iconLarge(mMapData), 0)
    }

    private fun sendNotificationLess(mRemoteMessage: RemoteMessage.Notification) {
        mNotificationManager.createNotificationLess(mRemoteMessage, iconLarge(mMapData))
    }

    private fun iconLarge(mMapData: Map<String, String>?): Bitmap? {
        return mMapData?.let {
            val url: URL? = URL(mMapData["picture_url"])
            url?.let {
                return BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } ?: BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.event_logo)
        } ?: BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.event_logo)
    }
}