package com.ipati.dev.castleevent.fragment.loading

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.fragment.CalendarFragment
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import kotlinx.android.synthetic.main.activity_config_setting_permission_dialog_fragment.*

class ConfigShowSettingPermissionDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_config_setting_permission_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        arguments?.let {
            title_confirm_dialog_calendar.text = arguments.getString(titleObject)
            msg_confirm_dialog_calendar.text = arguments.getString(msgObject)
        }

        bt_confirm_add_google_calendar.setOnClickListener {
            val listDetailFragment: Fragment? = activity.supportFragmentManager.findFragmentByTag("ListDetailFragment")
            val calendarFragment: Fragment? = activity.supportFragmentManager.findFragmentByTag("ListCalendarFragment")

            listDetailFragment?.let {
                (listDetailFragment as ListDetailEventFragment).apply {
                    setOnPositiveListener()
                }
            }

            calendarFragment?.let {
                (calendarFragment as CalendarFragment).apply {
                    setOnClickPositiveRequestPermission()
                }
            }

            dialog.dismiss()
        }

        bt_cancel_add_google_calendar.setOnClickListener {
            val fragmentListDetail: Fragment? = activity.supportFragmentManager.findFragmentByTag("ListDetailFragment")
            val calendarFragment: Fragment? = activity.supportFragmentManager.findFragmentByTag("ListCalendarFragment")

            fragmentListDetail?.let {
                (fragmentListDetail as ListDetailEventFragment).apply {
                    setOnNegativeListener()
                }
            }

            calendarFragment?.let {
                (calendarFragment as CalendarFragment).apply {
                    setOnClickNegativePermission()
                }
            }

            dialog.dismiss()
        }
    }

    companion object {
        var titleObject = "title"
        var msgObject = "msg"
        fun newInstance(title: String, msg: String): ConfigShowSettingPermissionDialogFragment {
            return ConfigShowSettingPermissionDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(titleObject, title)
                    putString(msg, msgObject)
                }
            }
        }
    }
}
