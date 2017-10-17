package com.ipati.dev.castleevent.utill

import android.support.v4.app.FragmentActivity
import com.ipati.dev.castleevent.fragment.loading.ConfigShowSettingPermissionDialogFragment
import com.ipati.dev.castleevent.fragment.loading.DialogConfirmFragment
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment

class DialogManager(var activity: FragmentActivity) {
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var confirmDialog: DialogConfirmFragment
    private lateinit var confirmAddCalendar: ConfigShowSettingPermissionDialogFragment

    fun onShowLoadingDialog(title: String): LoadingDialogFragment {
        loadingDialog = LoadingDialogFragment.newInstance(title, false)
        return loadingDialog.apply {
            show(activity.supportFragmentManager, "LoadingFragment")
        }
    }

    fun onShowConfirmDialog(msg: String): DialogConfirmFragment {
        confirmDialog = DialogConfirmFragment.newInstance(msg)
        return confirmDialog.apply {
            show(activity.supportFragmentManager, "ConfirmFragment")
        }
    }

    fun onDismissLoadingDialog() {
        return loadingDialog.dismiss()
    }

    fun onDismissConfirmDialog() {
        return confirmDialog.dismiss()
    }

    fun onShowConfirmDialogCalendar(title: String, msg: String): ConfigShowSettingPermissionDialogFragment {
        confirmAddCalendar = ConfigShowSettingPermissionDialogFragment.newInstance(title, msg)
        return confirmAddCalendar.apply {
            show(activity.supportFragmentManager, "ConfirmAddCalendar")
        }
    }
}