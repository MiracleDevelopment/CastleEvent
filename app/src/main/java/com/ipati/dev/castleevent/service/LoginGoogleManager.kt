package com.ipati.dev.castleevent.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.ipati.dev.castleevent.authCredential.googleAuthCredential

var SignInGoogle: Int = 1010
    get() = field

fun loginGoogleSignInDialog(activity: FragmentActivity) {
    val googleSignInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleApiService(activity))
    activity.startActivityForResult(googleSignInIntent, 1010)
}

fun googleSignInOptionsApi(): GoogleSignInOptions {
    val googleLoginManager: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .build()
    return googleLoginManager
}

fun googleApiService(activity: FragmentActivity): GoogleApiClient? {
    val mGoogleApiClient: GoogleApiClient? = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(p0: Bundle?) {

                }

                override fun onConnectionSuspended(p0: Int) {

                }
            })
            //Todo: Fix This Bug is null
            .addOnConnectionFailedListener { connectionResult -> Log.d("OnFailed", connectionResult.errorMessage.toString()) }
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptionsApi())
            .build()

    return mGoogleApiClient
}

fun callbackGoogleSignIn(activity: Activity, result: GoogleSignInResult) {
    googleAuthCredential(activity, result.signInAccount!!)
}