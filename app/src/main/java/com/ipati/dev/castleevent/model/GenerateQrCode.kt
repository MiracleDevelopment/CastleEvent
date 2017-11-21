package com.ipati.dev.castleevent.model

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import com.ipati.dev.castleevent.R
import net.glxn.qrgen.android.QRCode


class GenerateQrCode(context: Context) {
    private var mContext: Context = context

    fun bitMapQrCode(userAccount: String, eventId: String): Bitmap {
        val userInfo = userAccount + " : " + eventId
        return QRCode.from(userInfo).withSize(500, 500).withColor(ContextCompat.getColor(mContext, R.color.colorContentQrCode)
                , ContextCompat.getColor(mContext, R.color.colorBackgroundQrCode)).bitmap()
    }
}