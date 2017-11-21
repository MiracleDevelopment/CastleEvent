package com.ipati.dev.castleevent.fragment.loading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.*
import kotlinx.android.synthetic.main.activity_forgot_password_dialog_fragment.*
import kotlinx.android.synthetic.main.activity_login_fragment.*

class ForgotPasswordDialogFragment : DialogFragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_forgot_password_dialog_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tv_forgot_confirm.setOnClickListener(this)
        tv_forgot_cancel.setOnClickListener(this)
    }

    private fun requestPasswordFireBase() {
        val loadingDialogFragment = onShowLoadingDialog(activity, "กำลังส่งอีเมล์ให้ค่ะ กรุณรอสักครู่...", false)
        val fireBaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        fireBaseAuth.sendPasswordResetEmail(ed_add_email.text.toString()).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    onShowSuccessDialog(activity, "ทำการส่งพาสเวิอร์ดไปทางเมล์เรียบร้อยแล้วค่ะ")
                    loadingDialogFragment.onDismissDialog()
                    dialog.dismiss()
                }
                else -> {
                    onShowMissingDialog(activity, it.exception?.message.toString(), 1313)
                    loadingDialogFragment.onDismissDialog()
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_forgot_confirm -> {
                tv_forgot_email.isErrorEnabled = false
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(ed_add_email.text.toString()).matches()) {
                    requestPasswordFireBase()
                } else {
                    tv_forgot_email.error = "Empty Or Missing Patterns Email"
                }
            }

            R.id.tv_forgot_cancel -> {
                dialog.dismiss()
            }
        }
    }

    companion object {
        fun newInstance(): ForgotPasswordDialogFragment = ForgotPasswordDialogFragment().apply { arguments = Bundle() }
    }
}
