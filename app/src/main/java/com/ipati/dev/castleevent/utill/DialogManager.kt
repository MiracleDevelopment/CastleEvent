package com.ipati.dev.castleevent.utill

import android.support.v4.app.FragmentActivity
import com.ipati.dev.castleevent.fragment.loading.DialogConfirmFragment
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment


class DialogManager(activity: FragmentActivity) {
    private var activityDialog: FragmentActivity = activity
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var confirmDialog: DialogConfirmFragment


    fun onShowLoadingDialog(title: String): LoadingDialogFragment {
        loadingDialog = LoadingDialogFragment.newInstance(title, false)
        return loadingDialog.apply {
            show(activityDialog.supportFragmentManager, "LoadingFragment")
        }
    }

    fun onShowConfirmDialog(msg: String): DialogConfirmFragment {
        confirmDialog = DialogConfirmFragment.newInstance(msg)
        return confirmDialog.apply {
            show(activityDialog.supportFragmentManager, "ConfirmFragment")
        }
    }

    fun onDismissLoadingDialog() {
        return loadingDialog.dismiss()
    }

    fun onDismissConfirmDialog() {
        return confirmDialog.dismiss()
    }

}