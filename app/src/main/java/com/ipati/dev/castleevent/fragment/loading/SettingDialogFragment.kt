package com.ipati.dev.castleevent.fragment.loading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.firebase.messaging.FirebaseMessaging
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_setting_dialog_fragmnt.*

class SettingDialogFragment : DialogFragment() {
    var onChangeLanguage: ((status: Boolean) -> Unit?)? = null
    var onChangeNotification: ((status: Boolean) -> Unit?)? = null

    private val sharePreferenceSettingManager: SharePreferenceSettingManager by lazy {
        SharePreferenceSettingManager(context)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_setting_dialog_fragmnt, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        getDefaultSetting()

        setUpLanguage()
        setUpNotification()
    }

    private fun getDefaultSetting() {
        //Todo: Setting Default Language
        if (sharePreferenceSettingManager.defaultSharePreferenceLanguageManager()!!) {
            engLanguage()
        } else {
            thaiLanguage()
        }

        //Todo: Setting Default Notification
        if (sharePreferenceSettingManager.defaultSharePreferenceNotificationManager()!!) {
            openNotification()
        } else {
            closeNotification()
        }
    }


    private fun setUpLanguage() {
        tv_eng_language.setOnClickListener {
            sharePreferenceSettingManager.sharePreferenceLanguageManager(true)
            onChangeLanguage?.invoke(true)
            engLanguage()
            dialog.dismiss()
        }

        tv_thai_language.setOnClickListener {
            sharePreferenceSettingManager.sharePreferenceLanguageManager(false)
            onChangeLanguage?.invoke(false)
            thaiLanguage()
            dialog.dismiss()
        }
    }

    private fun setUpNotification() {
        tv_close_notification.setOnClickListener {
            sharePreferenceSettingManager.sharePreferenceNotificationManager(false)

            onChangeNotification?.invoke(false)
            closeNotification()
            dialog.dismiss()
        }

        tv_open_notification.setOnClickListener {
            sharePreferenceSettingManager.sharePreferenceNotificationManager(true)

            onChangeNotification?.invoke(true)
            openNotification()
            dialog.dismiss()
        }
    }

    private fun thaiLanguage() {
        tv_thai_language.isActivated = true
        tv_eng_language.isActivated = false
    }

    private fun engLanguage() {
        tv_thai_language.isActivated = false
        tv_eng_language.isActivated = true
    }

    private fun closeNotification() {
        tv_close_notification.isActivated = true
        tv_open_notification.isActivated = false
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news")
    }

    private fun openNotification() {
        tv_close_notification.isActivated = false
        tv_open_notification.isActivated = true
        FirebaseMessaging.getInstance().subscribeToTopic("news")
    }


    companion object {
        fun newInstance(): SettingDialogFragment {
            return SettingDialogFragment().apply {
                arguments = Bundle()
            }
        }
    }
}
