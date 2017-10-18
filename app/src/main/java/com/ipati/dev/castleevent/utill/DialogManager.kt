package com.ipati.dev.castleevent.utill

import android.support.v4.app.FragmentActivity
import com.ipati.dev.castleevent.fragment.loading.DialogConfirmFragment
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment


class DialogManager(activity: FragmentActivity) {
    private var mActivity: FragmentActivity = activity
    private lateinit var mLoadingDialog: LoadingDialogFragment
    private lateinit var mConfirmDialog: DialogConfirmFragment


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

}