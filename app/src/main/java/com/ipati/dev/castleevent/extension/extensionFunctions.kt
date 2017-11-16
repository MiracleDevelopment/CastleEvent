package com.ipati.dev.castleevent.extension

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.ipati.dev.castleevent.fragment.loading.*


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

fun View.getStringResource(resource: Int): String {
    return resources.getString(resource)
}

fun Context.pxToDp(specificWidth: Int): Int = ((specificWidth / resources.displayMetrics.density) * resources.displayMetrics.density).toInt()


fun onShowSettingDialog(supportFragmentManager: FragmentManager): SettingDialogFragment {
    return SettingDialogFragment.newInstance().apply {
        show(supportFragmentManager, "SettingFragmentDialog")
    }
}


fun onShowSnackBar(view: View, msg: String) = Snackbar.make(view, msg, Toast.LENGTH_SHORT).show()

fun onShowLoadingDialog(activity: FragmentActivity, msg: String, statusLoading: Boolean): LoadingDialogFragment {
    return LoadingDialogFragment.newInstance(msg, statusLoading).apply {
        isCancelable = false
        show(activity.supportFragmentManager, "LoadingDialogFragment")
    }
}

fun onShowRegisterDialogFragment(supportFragmentManager: FragmentManager): RegisterDialogFragment {
    return RegisterDialogFragment.newInstance().apply {
        show(supportFragmentManager, "RegisterDialogFragment")
    }
}

fun FragmentManager.replaceFragment(frameLayout: Int, fragmentOwner: Fragment, tag: String) {
    beginTransaction().replace(frameLayout, fragmentOwner, tag).commitNow()
}

fun Context.onShowToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun onShowMissingDialog(activity: FragmentActivity, msg: String, codeMessage: Int): MissingDialogFragment {
    return MissingDialogFragment.newInstance(msg, codeMessage).apply {
        isCancelable = false
        show(activity.supportFragmentManager, "MissingDialogFragment")
    }
}

fun onShowQuestionDialog(activity: FragmentActivity, msg: String, code: Int): QuestionDialogFragment {
    return QuestionDialogFragment.newInstance(msg, code).apply {
        isCancelable = false
        show(activity.supportFragmentManager, "QuestionDialogFragment")
    }
}

fun onShowSuccessDialog(activity: FragmentActivity, msg: String) = SuccessDialogFragment.newInstance(msg).apply {
    isCancelable = false
    show(activity.supportFragmentManager, "SuccessDialogFragment")
}






