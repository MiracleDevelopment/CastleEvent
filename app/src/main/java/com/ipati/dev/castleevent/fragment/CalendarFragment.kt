package com.ipati.dev.castleevent.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.activity_calendar_fragment.*
import java.util.*

class CalendarFragment : Fragment(), View.OnClickListener {
    var mCalendar: Calendar = Calendar.getInstance()
    lateinit var monthDefault: String
    lateinit var monthScroll: String
    lateinit var dateScroll: String
    lateinit var event: Event
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialCalendar()
        defaultMonth()

        tv_header_month.text = defaultMonth()
        addEvent()
    }

    private fun defaultMonth(): String {
        monthDefault = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        return monthDefault
    }

    private fun initialCalendar() {
        compat_calendar_view.displayOtherMonthDays(true)
        compat_calendar_view.setFirstDayOfWeek(Calendar.WEDNESDAY)
        compat_calendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                mCalendar.time = dateClicked
                tv_calendar_select_date.text = mCalendar.get(Calendar.DATE).toString()
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                mCalendar.time = firstDayOfNewMonth
                monthScroll = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                dateScroll = mCalendar.get(Calendar.DATE).toString()
                tv_header_month.text = monthScroll
                tv_calendar_select_date.text = dateScroll
            }
        })
    }

    private fun addEvent() {
        mCalendar.timeZone = TimeZone.getDefault()
        mCalendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND)
        event = Event(ContextCompat.getColor(context, R.color.colorEvent), mCalendar.timeInMillis, "Hello Event")
        compat_calendar_view.addEvent(event, true)

    }

    override fun onClick(p0: View?) {

    }

    companion object {
        fun newInstance(): CalendarFragment {
            val mCalendarFragment = CalendarFragment()
            val bundle = Bundle()
            mCalendarFragment.arguments = bundle
            return mCalendarFragment
        }
    }
}
