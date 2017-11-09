package com.ipati.dev.castleevent.fragment.loading

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.Toast
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.activity_date_time_picker_dialog_fragment.*
import java.util.*

class DateTimePickerDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_date_time_picker_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        setUpDatePicker()
    }

    private fun setUpDatePicker() {
        date_picker.init(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH) { p0, p1, p2, p3 ->

        }
    }

    companion object {
        fun newInstance(): DateTimePickerDialogFragment = DateTimePickerDialogFragment().apply { arguments = Bundle() }
    }
}
