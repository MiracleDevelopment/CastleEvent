package com.ipati.dev.castleevent.service

import android.app.Activity
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.ipati.dev.castleevent.authCredential.facebookAuthCredential
import com.ipati.dev.castleevent.model.LoadingListener
import java.util.*

var publicFacebookTag: String = "public_profile"
var cancelMsg: String = "Cancel"

var loginManager: LoginManager = LoginManager.getInstance()
    get() = field
var callbackManager: CallbackManager = CallbackManager.Factory.create()
    get() = field

var loadingListener: LoadingListener? = null

fun LoginFacebook(activity: Activity) {
    loginManager.logInWithReadPermissions(activity, Arrays.asList(publicFacebookTag))

    loadingListener = activity as LoadingListener
    loadingListener?.onShowLoading(true)
}

fun RegisterCallbackFacebook(activity: Activity, callbackManager: CallbackManager): LoginManager {
    loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            facebookAuthCredential(loadingListener, activity, result?.accessToken!!)
        }

        override fun onError(error: FacebookException?) {
            loadingListener?.onHindLoading(false)
            Toast.makeText(activity, error?.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            loadingListener?.onHindLoading(false)
            Toast.makeText(activity, cancelMsg, Toast.LENGTH_SHORT).show()
        }
    })
    return loginManager
}

fun CallbackManagerFacebook(): CallbackManager {
    return callbackManager
}


