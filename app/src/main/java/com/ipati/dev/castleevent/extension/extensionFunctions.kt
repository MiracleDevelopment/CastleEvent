package com.ipati.dev.castleevent.extension

import android.support.v4.app.FragmentActivity
import android.widget.EditText
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment


fun EditText.toStrEditText(): String {
    return text.toString()
}

fun LoadingDialogFragment.onShowDialog(activity: FragmentActivity) {
    isCancelable = false
    show(activity.supportFragmentManager, "LoadingDialogFragment")
}

fun LoadingDialogFragment.onDismissDialog() {
    dismiss()
}