package com.ipati.dev.castleevent.fragment.loading

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.matrixHeightPx
import com.ipati.dev.castleevent.extension.matrixWidthPx
import com.ipati.dev.castleevent.model.OnMissingConfirm
import kotlinx.android.synthetic.main.activity_missing_dialog_fragment.*

class MissingDialogFragment : DialogFragment() {
    private var onMissingDialogConfirm: OnMissingConfirm? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_missing_dialog_fragment, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.attributes.windowAnimations = R.style.AnimationDialogFragment

        arguments?.let {
            tv_msg_missing_dialog_fragment.text = arguments.getString(msgObject)
        }
        sizeImageAlert()
    }

    private fun sizeImageAlert() {
        im_logo_missing.layoutParams.width = context.matrixWidthPx(200)
        im_logo_missing.layoutParams.height = context.matrixHeightPx(200)
        tv_accept_cancel_missing_dialog.layoutParams.width = context.matrixWidthPx(context.resources.displayMetrics.widthPixels - 350)

        tv_accept_cancel_missing_dialog.setOnClickListener {
            onMissingDialogConfirm = context as ListDetailEventActivity
            onMissingDialogConfirm?.let {
                (onMissingDialogConfirm as ListDetailEventActivity).onMissingDialogConfirm()
            }

        }
    }

    companion object {
        var msgObject = "msg"
        fun newInstance(msg: String): MissingDialogFragment {
            return MissingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(msgObject, msg)
                }
            }
        }
    }
}
