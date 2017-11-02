package com.ipati.dev.castleevent.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import java.util.*


class LocalHelper {
    private lateinit var sharePreferenceSettingManager: SharePreferenceSettingManager
    fun onAttach(context: Context): Context {
        val language: String = Locale.getDefault().language

        sharePreferenceSettingManager = SharePreferenceSettingManager(context)

        sharePreferenceSettingManager.defaultSharePreferenceLanguageManager()?.let {
            if (sharePreferenceSettingManager.defaultSharePreferenceLanguageManager()!!) {
                return setLocal(context, "en")
            }
            return setLocal(context, "th")
        } ?: setLocal(context, language)
        return setLocal(context, language)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        return setLocal(context, defaultLanguage)
    }

    fun setLocal(context: Context, language: String): Context {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return upgradeResource(context, language)
        }

        return upgradeResourceLegacy(context, language)

    }

    private fun upgradeResource(context: Context, language: String): Context {
        val local = Locale(language)
        val configuration: Configuration = context.resources.configuration

        Locale.setDefault(local)
        configuration.setLocale(local)

        return context.createConfigurationContext(configuration)
    }

    private fun upgradeResourceLegacy(context: Context, language: String): Context {
        val local = Locale(language)
        val configuration: Configuration = context.resources.configuration

        Locale.setDefault(local)
        configuration.setLocale(local)

        return context.createConfigurationContext(configuration)
    }
}