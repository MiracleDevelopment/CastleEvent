package com.ipati.dev.castleevent.fragment.loading

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.R

class LogOutFragmentDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_log_out_fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {

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
