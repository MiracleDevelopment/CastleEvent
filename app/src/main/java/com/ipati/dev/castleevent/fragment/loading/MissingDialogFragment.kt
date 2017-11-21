package com.ipati.dev.castleevent.fragment.loading

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.onShowToast
import com.ipati.dev.castleevent.extension.pxToDp
import com.ipati.dev.castleevent.extension.pxToDp
import com.ipati.dev.castleevent.model.OnMissingConfirm
import icepick.Icepick
import icepick.State
import kotlinx.android.synthetic.main.activity_missing_dialog_fragment.*

class MissingDialogFragment : DialogFragment() {
    private var onMissingDialogConfirm: OnMissingConfirm? = null
    private var codeMessage: Int = 0
    var callBackReAuthentication: (() -> Unit)? = null
    var callBackNetWork: (() -> Unit?)? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_missing_dialog_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        arguments?.let {
            tv_msg_missing_dialog_fragment.text = arguments.getString(msgObject)
            codeMessage = arguments.getInt(codeMessageObject)
        }

        sizeImageAlert()
    }

    private fun sizeImageAlert() {
        im_logo_missing.layoutParams.width = context.pxToDp(180)
        im_logo_missing.layoutParams.height = context.pxToDp(180)

        tv_accept_cancel_missing_dialog.layoutParams.width = context
                .pxToDp(context.resources.displayMetrics.widthPixels - 350)

        tv_accept_cancel_missing_dialog.setOnClickListener {
            when (codeMessage) {
                codeReAuthentication -> {
                    callBackReAuthentication?.invoke()
                    dialog.dismiss()
                }

                codeNetWork -> {
                    callBackNetWork?.invoke()
                    dialog.dismiss()
                }

                codeResetPassword -> {
                    dialog.dismiss()
                }

                else -> {
                    confirmTicket()
                }
            }
        }
    }

    private fun confirmTicket() {
        onMissingDialogConfirm = context as ListDetailEventActivity
        onMissingDialogConfirm?.let {
            (onMissingDialogConfirm as ListDetailEventActivity).onMissingDialogConfirm()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            tv_msg_missing_dialog_fragment.text = savedInstanceState.getString(msgObject)
            codeMessage = savedInstanceState.getInt(codeMessageObject)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            outState.putString(msgObject, tv_msg_missing_dialog_fragment.text.toString())
            outState.putInt(codeMessageObject, codeMessage)
        }
    }

    companion object {
        private const val msgObject = "msg"
        private const val codeMessageObject = "codeMessage"
        private const val codeReAuthentication: Int = 1112
        private const val codeNetWork: Int = 1010
        private const val codeResetPassword: Int = 1313

        fun newInstance(msg: String, codeMessage: Int): MissingDialogFragment {
            return MissingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(msgObject, msg)
                    putInt(codeMessageObject, codeMessage)
                }
            }
        }
    }
}
