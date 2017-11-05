package com.ipati.dev.castleevent.utill

import android.content.Context
import android.content.SharedPreferences


class SharePreferenceGoogleSignInManager(context: Context) {
    private var googleSignIn = "GoogleUsernameAccount"
    private var googleSignInIgnore = "GoogleUserIgnore"
    private var contextSharePreferenceGoogleSignIn: Context = context
    private lateinit var sharePreference: SharedPreferences
    private lateinit var sharePreferenceEditor: SharedPreferences.Editor

    fun sharePreferenceManager(username: String?) {
        sharePreference = contextSharePreferenceGoogleSignIn.getSharedPreferences(googleSignIn, Context.MODE_PRIVATE)
        sharePreferenceEditor = sharePreference.edit()
        sharePreferenceEditor.putString(googleSignIn, username)
        sharePreferenceEditor.apply()
    }


    fun defaultSharePreferenceManager(): String? {
        sharePreference = contextSharePreferenceGoogleSignIn.getSharedPreferences(googleSignIn, Context.MODE_PRIVATE)
        return sharePreference.getString(googleSignIn, null)
    }

    fun sharePreferenceManagerIgonre(username: String?) {
        sharePreference = contextSharePreferenceGoogleSignIn.getSharedPreferences(googleSignInIgnore, Context.MODE_PRIVATE)
        sharePreferenceEditor = sharePreference.edit()
        sharePreferenceEditor.putString(googleSignInIgnore, username)
        sharePreferenceEditor.apply()
    }

    fun defaultSharePreferenceManagerIgnore(): String? {
        sharePreference = contextSharePreferenceGoogleSignIn.getSharedPreferences(googleSignInIgnore, Context.MODE_PRIVATE)
        return sharePreference.getString(googleSignInIgnore, null)
    }
}