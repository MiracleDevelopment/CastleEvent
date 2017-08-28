package com.ipati.dev.castleevent.utill

import android.content.Context
import android.content.SharedPreferences


class SharePreferenceGoogleSignInManager(context: Context) {
    private var mGoogleSignIn = "GoogleUsernameAccount"
    private var mGoogleSignInIgnore = "GoogleUserIgnore"
    private var mContext: Context = context
    private lateinit var sharePreference: SharedPreferences
    private lateinit var sharePreferenceEditor: SharedPreferences.Editor

    fun sharePreferenceManager(username: String?) {
        sharePreference = mContext.getSharedPreferences(mGoogleSignIn, Context.MODE_PRIVATE)
        sharePreferenceEditor = sharePreference.edit()
        sharePreferenceEditor.putString(mGoogleSignIn, username)
        sharePreferenceEditor.apply()
    }


    fun defaultSharePreferenceManager(): String? {
        sharePreference = mContext.getSharedPreferences(mGoogleSignIn, Context.MODE_PRIVATE)
        return sharePreference.getString(mGoogleSignIn, "defaultUsername")
    }

    fun sharePreferenceManagerIgonre(username: String?) {
        sharePreference = mContext.getSharedPreferences(mGoogleSignInIgnore, Context.MODE_PRIVATE)
        sharePreferenceEditor = sharePreference.edit()
        sharePreferenceEditor.putString(mGoogleSignInIgnore, username)
        sharePreferenceEditor.apply()
    }

    fun defaultSharePreferenceManagerIgnore(): String? {
        sharePreference = mContext.getSharedPreferences(mGoogleSignInIgnore, Context.MODE_PRIVATE)
        return sharePreference.getString(mGoogleSignInIgnore, null)
    }
}