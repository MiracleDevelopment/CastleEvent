package com.ipati.dev.castleevent.service.FirebaseNotification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ipati.dev.castleevent.R
import java.net.URL

class FirebaseMessagingService : FirebaseMessagingService() {
    private var mapData: Map<String, String>? = null
    private var notificationManager: NotificationManager = NotificationManager(this)
    lateinit var remoteMessageNotification: RemoteMessage.Notification

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        remoteMessageNotification = p0?.notification!!
        mapData = p0.data

        if (Build.VERSION.SDK_INT >= 26) {
            sendNotification(remoteMessageNotification)
        } else {
            sendNotificationLess(remoteMessageNotification)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(mRemoteMessage: RemoteMessage.Notification) {
//        notificationManager.createNotificationManager(mRemoteMessage, iconLarge(mapData), 0)
    }

    private fun sendNotificationLess(mRemoteMessage: RemoteMessage.Notification) {
//        notificationManager.createNotificationLess(mRemoteMessage, iconLarge(mapData))
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