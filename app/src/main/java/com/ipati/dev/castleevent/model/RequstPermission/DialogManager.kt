package com.ipati.dev.castleevent.model.RequstPermission

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.ipati.dev.castleevent.fragment.loading.ConfigShowSettingPermissionDialogFragment

fun showDialogRequestSetting(activity: FragmentActivity, title: String, msg: String): ConfigShowSettingPermissionDialogFragment {
    return ConfigShowSettingPermissionDialogFragment.newInstance(title, msg).apply {
        arguments = Bundle().apply {
            putString("title", title)
            putString("msg", msg)
        }
        isCancelable = false
        show(activity.supportFragmentManager, "ConfigDialogFragment")
    }
}

