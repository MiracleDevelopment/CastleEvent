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


class RegisterDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_register_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.attributes.windowAnimations = android.R.anim.anticipate_overshoot_interpolator
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    companion object {
        fun newInstance(): RegisterDialogFragment {
            return RegisterDialogFragment().apply {
                arguments = Bundle()
            }
        }
    }

}
