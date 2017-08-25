package com.ipati.dev.castleevent.service

import android.app.Activity
import android.content.Context
import com.facebook.login.LoginManager
import com.ipati.dev.castleevent.fragment.LoginFragment

import com.ipati.dev.castleevent.model.LoadingListener
import java.util.*

var publicFacebookTag: String = "public_profile"
var cancelMsg: String = "Cancel"
var loadingListener: LoadingListener? = null

fun LoginFacebook(context: Context, activity: LoginFragment) {
    LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(publicFacebookTag))

    loadingListener = context as LoadingListener
    loadingListener?.onShowLoading(true)
}





