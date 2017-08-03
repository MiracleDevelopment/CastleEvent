package com.ipati.dev.castleevent.service

import android.app.Activity
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.ipati.dev.castleevent.authCredential.FacebookAuthCredential
import java.util.*

var publicFacebookTag: String = "public_profile"
var cancelMsg: String = "Cancel"
var loginManager: LoginManager = LoginManager.getInstance()
    get() = field
var callbackManager: CallbackManager = CallbackManager.Factory.create()
    get() = field

fun LoginFacebook(activity: Activity) {
    loginManager.logInWithReadPermissions(activity, Arrays.asList(publicFacebookTag))
}

fun RegisterCallbackFacebook(activity: Activity, callbackManager: CallbackManager): LoginManager {
    loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            FacebookAuthCredential(activity, result?.accessToken!!)
        }

        override fun onError(error: FacebookException?) {
            Toast.makeText(activity, error?.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            Toast.makeText(activity, cancelMsg, Toast.LENGTH_SHORT).show()
        }

    })
    return loginManager
}

fun CallbackManagerFacebook(): CallbackManager {
    return callbackManager
}


