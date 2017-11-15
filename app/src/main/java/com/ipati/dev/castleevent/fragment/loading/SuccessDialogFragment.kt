package com.ipati.dev.castleevent.fragment.loading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.activity_sucess_dialog_fragment.*

class SuccessDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_sucess_dialog_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.attributes.windowAnimations = R.style.dialogAnimation

        arguments?.let {
            tv_msg_description.text = arguments.getString(msgObject)
        }

        tv_success_dialog.setOnClickListener {
            dialog.dismiss()
        }
    }

    companion object {
        private const val msgObject: String = "msg"
        fun newInstance(msg: String): SuccessDialogFragment = SuccessDialogFragment().apply {
            arguments = Bundle().apply {
                putString(msgObject, msg)

            }
        }
    }
}
