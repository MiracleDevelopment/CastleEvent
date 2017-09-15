package com.ipati.dev.castleevent.fragment.loading


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.activity_loading_dialog_fragment.*

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_loading_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        arguments?.let {
            tv_loading_header_dialog_fragment.text = arguments.getString(titleObject)
        }
    }

    companion object {
        var titleObject = "title"
        fun newInstance(title: String): LoadingDialogFragment {
            return LoadingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(titleObject, title)
                }
            }
        }
    }
}
