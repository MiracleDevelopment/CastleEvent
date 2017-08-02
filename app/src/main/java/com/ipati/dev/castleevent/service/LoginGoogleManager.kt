package com.ipati.dev.castleevent.service

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient

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
            .enableAutoManage(activity) { connectionResult ->
                Toast.makeText(activity, connectionResult.errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
            .addApi(Auth.GOOGLE_SIGN_IN_API, GoogleSignInOptionsApi())
            .build()

    return mGoogleApiClient
}

fun CallbackGoogleSignIn(context: Context, result: GoogleSignInResult) {
    Toast.makeText(context, result.signInAccount.toString(), Toast.LENGTH_SHORT).show()
}