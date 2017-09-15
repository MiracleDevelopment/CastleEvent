package com.ipati.dev.castleevent.utill

import android.support.v4.app.FragmentActivity
import com.ipati.dev.castleevent.fragment.loading.DialogConfirmFragment
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment

class DialogManager(activity: FragmentActivity) {
    private var mActivity: FragmentActivity = activity
    private lateinit var mLoadingDialog: LoadingDialogFragment
    private lateinit var mConfirmDialog: DialogConfirmFragment

    fun onShowLoadingDialog(title: String): LoadingDialogFragment {
        mLoadingDialog = LoadingDialogFragment.newInstance(title)
        return mLoadingDialog.apply {
            show(mActivity.supportFragmentManager, "LoadingFragment")
        }
    }

    fun onShowConfirmDialog(msg: String): DialogConfirmFragment {
        mConfirmDialog = DialogConfirmFragment.newInstance(msg)
        return mConfirmDialog.apply {
            show(mActivity.supportFragmentManager, "ConfirmFragment")
        }
    }

    fun onDismissLoadingDialog() {
        return mLoadingDialog.dismiss()
    }

    fun onDismissConfirmDialog() {
        return mConfirmDialog.dismiss()
    }
}