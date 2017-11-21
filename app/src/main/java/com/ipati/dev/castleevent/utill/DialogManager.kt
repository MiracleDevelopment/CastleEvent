package com.ipati.dev.castleevent.utill

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.ipati.dev.castleevent.fragment.loading.DialogConfirmFragment
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.fragment.loading.MissingDialogFragment


class DialogManager( activity: FragmentActivity) {
    private var activityDialog: FragmentActivity? = activity
    var loadingDialog: LoadingDialogFragment? = null
    var confirmDialog: DialogConfirmFragment? = null
    var missingDialog: MissingDialogFragment? = null

    fun onShowLoadingDialog(title: String): LoadingDialogFragment? {
        loadingDialog = LoadingDialogFragment.newInstance(title, false)
        loadingDialog?.isCancelable = false
        return loadingDialog?.apply {
            val fragmentManager = activityDialog?.supportFragmentManager
                    ?.beginTransaction()
            show(fragmentManager, tagLoadingFragment)
        }
    }

    fun onShowConfirmDialog(msg: String): DialogConfirmFragment? {
        confirmDialog = DialogConfirmFragment.newInstance(msg)
        confirmDialog?.isCancelable = false
        return confirmDialog?.apply {
            val fragmentManager = activityDialog?.supportFragmentManager
                    ?.beginTransaction()
                    ?.addToBackStack(tagConfirmFragment)
            show(fragmentManager, tagConfirmFragment)
        }
    }

    fun onShowMissingDialog(msg: String, codeMessage: Int): MissingDialogFragment? {
        missingDialog = MissingDialogFragment.newInstance(msg, codeMessage)
        missingDialog?.isCancelable = false
        return missingDialog?.apply {
            val fragmentManager = activityDialog?.supportFragmentManager
                    ?.beginTransaction()
                    ?.addToBackStack(tagMissingDialogFragment)
            this.show(fragmentManager, tagMissingDialogFragment)
        }
    }

    fun onDismissLoadingDialog() {
        loadingDialog?.let {
            loadingDialog?.dismiss()
        } ?: if (activityDialog != null) activityDialog?.supportFragmentManager?.popBackStack()
    }

    fun onDismissConfirmDialog() {
        confirmDialog?.let {
            confirmDialog?.dismiss()
        } ?: activityDialog?.supportFragmentManager?.popBackStack()
    }

    fun onDismissMissingDialog() {
        missingDialog?.let {
            missingDialog?.dismiss()
        } ?: activityDialog?.supportFragmentManager?.popBackStack()
    }

    companion object {
        private const val tagLoadingFragment = "LoadingFragment"
        private const val tagConfirmFragment = "ConfirmFragment"
        private const val tagMissingDialogFragment = "MissingDialogFragment"
    }

}