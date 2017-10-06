package com.ipati.dev.castleevent.fragment.loading

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.OnLogOutSystem
import kotlinx.android.synthetic.main.activity_log_out_fragment_dialog.*

class LogOutFragmentDialog : DialogFragment(), View.OnClickListener {
    private var mLogOutListener: OnLogOutSystem? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_log_out_fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        arguments?.let {
            tv_header_dialog_logout.text = arguments.getString(titleObject)
            tv_msg_dialog_logout.text = arguments.getString(msgObject)
        }

        tv_accept_dialog_logout.setOnClickListener { viewClickable: View -> onClick(viewClickable) }
        tv_cancel_dialog_logout.setOnClickListener { viewClickable: View -> onClick(viewClickable) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            tv_header_dialog_logout.text = savedInstanceState.getString(titleObject)
            tv_msg_dialog_logout.text = savedInstanceState.getString(msgObject)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            outState.apply {
                putString(titleObject, tv_header_dialog_logout.text.toString())
                putString(msgObject, tv_msg_dialog_logout.text.toString())
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_accept_dialog_logout -> {
                mLogOutListener = activity as ListEventActivity
                mLogOutListener?.let {
                    mLogOutListener?.logOutApplication()
                    dialog.dismiss()
                }

            }

            R.id.tv_cancel_dialog_logout -> {
                dialog.dismiss()
            }
        }
    }

    companion object {
        var titleObject = "title"
        var msgObject = "msg"

        fun newInstance(title: String, msg: String): LogOutFragmentDialog {
            return LogOutFragmentDialog().apply {
                arguments = Bundle().apply {
                    putString(titleObject, title)
                    putString(msgObject, msg)
                }
            }
        }
    }
}
