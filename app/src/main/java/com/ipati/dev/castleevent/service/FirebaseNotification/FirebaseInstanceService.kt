package com.ipati.dev.castleevent.service.FirebaseNotification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class FirebaseInstanceService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        sendRegisterToServer(FirebaseInstanceId.getInstance().token)

    }

    private fun sendRegisterToServer(token: String?) {
        //Sending To Server//
        Log.d("token", token)
    }
}