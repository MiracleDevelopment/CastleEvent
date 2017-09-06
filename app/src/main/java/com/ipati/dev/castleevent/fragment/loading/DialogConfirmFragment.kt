package com.ipati.dev.castleevent.fragment.loading

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.LoadingDialogListener
import kotlinx.android.synthetic.main.activity_dialog_confirm_fragment.*

class DialogConfirmFragment : DialogFragment() {
    private var mLoadingListener: LoadingDialogListener? = null
    private var restoreMsg: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            restoreMsg = savedInstanceState.getString(objectKey)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_dialog_confirm_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        defaultMsg()
        tv_confirm_dialog_fragment.setOnClickListener {
            mLoadingListener = activity as ListDetailEventActivity
            if (mLoadingListener != null) {
                (mLoadingListener as ListDetailEventActivity).onPositiveClickable(true)
            } else {
                dialog.dismiss()
            }
        }

        tv_cancel_dialog_fragment.setOnClickListener {
            mLoadingListener = activity as ListDetailEventActivity
            if (mLoadingListener != null) {
                (mLoadingListener as ListDetailEventActivity).onNegativeClickable(false)
            } else {
                dialog.dismiss()
            }
        }
    }

    private fun defaultMsg() {
        if (arguments != null) {
            val msg = arguments.getString(objectKey)
            tv_msg_dialog_confirm.text = msg
        } else {
            restoreMsg?.let {
                tv_msg_dialog_confirm.text = restoreMsg
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(objectKey, arguments.getString(objectKey))
    }

    companion object {
        var objectKey = "DialogConfirmFragment"
        fun newInstance(msg: String): DialogConfirmFragment {
            return DialogConfirmFragment().apply {
                arguments = Bundle().apply {
                    putString(objectKey, msg)
                }
            }
        }
    }
}
