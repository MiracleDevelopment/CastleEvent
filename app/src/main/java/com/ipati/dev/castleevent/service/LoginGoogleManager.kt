package com.ipati.dev.castleevent.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.ipati.dev.castleevent.authCredential.GoogleAuthCredential

var SignInGoogle: Int = 1010
    get() = field

fun LoginGoogleSignInDialog(activity: FragmentActivity) {
    val googleSignInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(GoogleApiService(activity))
    activity.startActivityForResult(googleSignInIntent, 1010)
}

fun GoogleSignInOptionsApi(): GoogleSignInOptions {
    val googleLoginManager: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .build()
    return googleLoginManager
}

fun GoogleApiService(activity: FragmentActivity): GoogleApiClient? {
    val mGoogleApiClient: GoogleApiClient? = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(p0: Bundle?) {

                }

                override fun onConnectionSuspended(p0: Int) {

                }
            })
            //Todo: Fix This Bug is null
            .addOnConnectionFailedListener { connectionResult -> Log.d("OnFailed", connectionResult.errorMessage.toString()) }
            .addApi(Auth.GOOGLE_SIGN_IN_API, GoogleSignInOptionsApi())
            .build()

    return mGoogleApiClient
}

fun CallbackGoogleSignIn(activity: Activity, result: GoogleSignInResult) {
    GoogleAuthCredential(activity, result.signInAccount!!)
}