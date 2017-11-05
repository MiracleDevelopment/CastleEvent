package com.ipati.dev.castleevent.utill

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class SharePreferenceSettingManager(context: Context) {
    private val sharePreferenceNotificationKey: String = "notification"
    private val sharePreferenceLanguageKey: String = "Language"
    private lateinit var sharePreferenceNotification: SharedPreferences
    private lateinit var sharePreferenceNotificationEditor: SharedPreferences.Editor
    private var contextSharePreferenceManager: Context = context

    init {
        this.contextSharePreferenceManager = context
    }

    @SuppressLint("CommitPrefEdits")
    fun sharePreferenceNotificationManager(checkState: Boolean) {
        sharePreferenceNotification = contextSharePreferenceManager.getSharedPreferences(sharePreferenceNotificationKey, Context.MODE_PRIVATE)
        sharePreferenceNotificationEditor = sharePreferenceNotification.edit()
        sharePreferenceNotificationEditor.putBoolean(sharePreferenceNotificationKey, checkState)
        sharePreferenceNotificationEditor.apply()
    }

    fun sharePreferenceLanguageManager(checkState: Boolean) {
        sharePreferenceNotification = contextSharePreferenceManager.getSharedPreferences(sharePreferenceLanguageKey, Context.MODE_PRIVATE)
        sharePreferenceNotificationEditor = sharePreferenceNotification.edit()
        sharePreferenceNotificationEditor.putBoolean(sharePreferenceLanguageKey, checkState)
        sharePreferenceNotificationEditor.apply()
    }

    fun defaultSharePreferenceNotificationManager(): Boolean? {
        sharePreferenceNotification = contextSharePreferenceManager.getSharedPreferences(sharePreferenceNotificationKey, Context.MODE_PRIVATE)
        return sharePreferenceNotification.getBoolean(sharePreferenceNotificationKey, true)
    }

    fun defaultSharePreferenceLanguageManager(): Boolean? {
        sharePreferenceNotification = contextSharePreferenceManager.getSharedPreferences(sharePreferenceLanguageKey, Context.MODE_PRIVATE)
        return sharePreferenceNotification.getBoolean(sharePreferenceLanguageKey, true)
    }

}
