package com.ipati.dev.castleevent.fragment.loading

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.LoginActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.pxToDp
import com.ipati.dev.castleevent.extension.pxToDp
import kotlinx.android.synthetic.main.activity_question_dialog_fragment.*

class QuestionDialogFragment : DialogFragment(), View.OnClickListener {
    private var codeMsg: Int = 0
    var callBackQuestion: (() -> Any?)? = null
    var callBackMyOrderQuestion: (() -> Unit?)? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_question_dialog_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        im_error_outline_logo.layoutParams.width = context.pxToDp(180)
        im_error_outline_logo.layoutParams.height = context.pxToDp(180)

        arguments?.let {
            tv_msg_question_dialog.text = arguments.getString(msgObject)
            codeMsg = arguments.getInt(codeObject)
        }

        tv_accept_question_dialog.setOnClickListener(this)
        tv_cancel_bt_question_dialog.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_accept_question_dialog -> {
                when (codeMsg) {
                    REQUEST_RE_LOGIN -> {
                        FirebaseAuth.getInstance().signOut()
                        val intentLogin = Intent(context, LoginActivity::class.java)
                        startActivity(intentLogin)
                        dialog.dismiss()
                        activity.finish()
                    }

                    REQUEST_DELETE_FAVORITE -> {
                        callBackQuestion?.invoke()
                        dialog.dismiss()
                    }

                    REQUEST_MY_ORDER -> {
                        callBackMyOrderQuestion?.invoke()
                        dialog.dismiss()
                    }
                }
            }

            R.id.tv_cancel_bt_question_dialog -> {
                dialog.dismiss()
            }
        }
    }

    companion object {
        private const val REQUEST_RE_LOGIN: Int = 1001
        private const val REQUEST_DELETE_FAVORITE: Int = 1002
        private const val REQUEST_MY_ORDER: Int = 1003

        private const val msgObject: String = "msg"
        private const val codeObject: String = "code"
        fun newInstance(msg: String, code: Int): QuestionDialogFragment = QuestionDialogFragment().apply {
            arguments = Bundle().apply {
                putString(msgObject, msg)
                putInt(codeObject, code)
            }
        }
    }
}
