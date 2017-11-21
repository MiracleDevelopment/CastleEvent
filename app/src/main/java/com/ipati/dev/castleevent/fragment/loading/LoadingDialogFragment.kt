package com.ipati.dev.castleevent.fragment.loading


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.pxToDp

import kotlinx.android.synthetic.main.activity_loading_dialog_fragment.*

class LoadingDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_loading_dialog_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        arguments?.let {
            tv_loading_header_dialog_fragment.text = arguments.getString(titleObject)

            lottie_view_animation_loading_dialog.layoutParams.height = context.pxToDp(350)
            lottie_view_animation_loading_dialog.setAnimation("loading_animation.json")
            lottie_view_animation_loading_dialog.loop(true)
            lottie_view_animation_loading_dialog.playAnimation()

            if (arguments.getBoolean(statusProgressObject)) {
                tv_loading_progress_dialog_fragment.visibility = View.VISIBLE
            } else {
                tv_loading_progress_dialog_fragment.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setProgressUploadTaskPhoto(progressValue: Int) {
        tv_loading_progress_dialog_fragment.text = "$progressValue %"
    }

    companion object {
        var titleObject = "title"
        var statusProgressObject = "statusProgress"
        fun newInstance(title: String, statusProgress: Boolean): LoadingDialogFragment {
            return LoadingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(titleObject, title)
                    putBoolean(statusProgressObject, statusProgress)
                }
            }
        }


    }
}
