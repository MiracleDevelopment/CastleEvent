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
import kotlinx.android.synthetic.main.activity_question_dialog_fragment.*

class QuestionDialogFragment : DialogFragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_question_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        arguments?.let {
            tv_msg_question_dialog.text = arguments.getString(msgObject)
        }
        tv_accept_question_dialog.setOnClickListener(this)
        tv_cancel_bt_question_dialog.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_accept_question_dialog -> {
                FirebaseAuth.getInstance().signOut()
                val intentLogin = Intent(context, LoginActivity::class.java)
                startActivity(intentLogin)
                dialog.dismiss()
                activity.finish()
            }

            R.id.tv_cancel_bt_question_dialog -> {
                dialog.dismiss()
            }
        }
    }

    companion object {
        private const val msgObject: String = "msg"
        fun newInstance(msg: String): QuestionDialogFragment = QuestionDialogFragment().apply {
            arguments = Bundle().apply {
                putString(msgObject, msg)
            }
        }
    }
}
