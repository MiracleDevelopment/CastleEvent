package com.ipati.dev.castleevent.utill

import android.support.v4.app.FragmentActivity
import com.ipati.dev.castleevent.fragment.LoginFragment
import com.ipati.dev.castleevent.fragment.loading.DialogConfirmFragment
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.fragment.loading.MissingDialogFragment


class DialogManager(activity: FragmentActivity) {
    private var activityDialog: FragmentActivity = activity
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var confirmDialog: DialogConfirmFragment
    private lateinit var missingDialog: MissingDialogFragment

    fun onShowLoadingDialog(title: String): LoadingDialogFragment {
        loadingDialog = LoadingDialogFragment.newInstance(title, false)
        loadingDialog.isCancelable = false
        return loadingDialog.apply {
            show(activityDialog.supportFragmentManager, "LoadingFragment")
        }
    }

    fun onShowConfirmDialog(msg: String): DialogConfirmFragment {
        confirmDialog = DialogConfirmFragment.newInstance(msg)
        confirmDialog.isCancelable = false
        return confirmDialog.apply {
            show(activityDialog.supportFragmentManager, "ConfirmFragment")
        }
    }

    fun onShowMissingDialog(msg: String): MissingDialogFragment {
        missingDialog = MissingDialogFragment.newInstance(msg)
        missingDialog.isCancelable = false
        return missingDialog.apply {
            show(activityDialog.supportFragmentManager, "MissingDialogFragment")
        }
    }

    fun onDismissLoadingDialog() {
        return loadingDialog.dismiss()
    }

    fun onDismissConfirmDialog() {
        return confirmDialog.dismiss()
    }

    fun onDismissMissingDialog() {
        return missingDialog.dismiss()
    }

}