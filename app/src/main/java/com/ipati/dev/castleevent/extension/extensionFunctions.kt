package com.ipati.dev.castleevent.extension

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.EditText
import com.bumptech.glide.load.engine.Resource
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

fun ContextCompat.getColor(context: Context, resource: Int) {
    getColor(context, resource)
}

fun View.getStringResource(resource: Int): String {
    return resources.getString(resource)
}

fun Context.matrixWidthPx(specificWidth: Int): Int {
    return ((specificWidth / resources.displayMetrics.density) * resources.displayMetrics.density).toInt()
}

fun Context.matrixHeightPx(specificHeight: Int): Int {
    return ((specificHeight / resources.displayMetrics.density) * resources.displayMetrics.density).toInt()
}