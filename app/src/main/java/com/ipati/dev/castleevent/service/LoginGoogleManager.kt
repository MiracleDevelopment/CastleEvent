package com.ipati.dev.castleevent.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.ipati.dev.castleevent.authCredential.googleAuthCredential
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager

var SignInGoogle: Int = 1010
    get() = field
var googleLoginManager: GoogleSignInOptions? = null
var googleApiClient: GoogleApiClient? = null
var serverClientSide: String = "816401657226-jq3a7du27lran3okem2j57l6jnl9q210.apps.googleusercontent.com"

fun loginGoogleSignInOption(activity: FragmentActivity) {
    val googleSignInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleApiService(activity))
    activity.startActivityForResult(googleSignInIntent, 1010)
}

fun googleSignInOptionsApi(): GoogleSignInOptions? {
    googleLoginManager = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientSide)
            .requestProfile()
            .requestEmail()
            .build()
    return googleLoginManager
}

fun googleApiService(activity: FragmentActivity): GoogleApiClient? {
    googleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(p0: Bundle?) {

                }

                override fun onConnectionSuspended(p0: Int) {

                }
            })

            //Todo: Fix This Bug is null
            .addOnConnectionFailedListener { connectionResult -> Log.d("OnFailed", connectionResult.errorMessage.toString()) }
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptionsApi()!!)
            .build()

    return googleApiClient
}

fun callbackGoogleSignIn(activity: Activity, result: GoogleSignInAccount, mGoogleSharedPreferences: SharePreferenceGoogleSignInManager) {
    googleAuthCredential(activity, result, mGoogleSharedPreferences)
}