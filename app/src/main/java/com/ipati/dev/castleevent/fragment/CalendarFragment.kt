package com.ipati.dev.castleevent.fragment

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.google.android.gms.common.ConnectionResult
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Events
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ListEventCalendarAdapter
import com.ipati.dev.castleevent.model.EventDetailModel
import com.ipati.dev.castleevent.model.GoogleCalendar.CalendarFragment.CalendarManager
import com.ipati.dev.castleevent.utill.*
import kotlinx.android.synthetic.main.activity_calendar_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment(), View.OnClickListener {
    private var REQUEST_ACCOUNT: Int = 1111
    private var maxCalendarHeight: Int = 346
    private var maxHeaderCalendar: Int = 312
    private var minCalendarHeight: Int = 256
    private var minHeaderCalendar: Int = 50

    private lateinit var mCalendarManager: CalendarManager
    private lateinit var mSharePreferenceManager: SharePreferenceGoogleSignInManager
    private lateinit var mListEventDateClick: List<com.github.sundeepk.compactcalendarview.domain.Event>
    private lateinit var mListEventCalendarAdapter: ListEventCalendarAdapter
    private lateinit var mSimpleDateFormatDateTime: SimpleDateFormat
    private lateinit var mSimpleDateFormatNickNameDate: SimpleDateFormat
    private lateinit var mSimpleDateFormatDateOfYear: SimpleDateFormat
    private lateinit var mSimpleDateFormat: SimpleDateFormat
    private lateinit var eventDetailModel: EventDetailModel
    private lateinit var mCalendarToday: Calendar
    private lateinit var mDateCurrent: Date
    private lateinit var monthDefault: String


    private var mListItemShow: ArrayList<EventDetailModel> = ArrayList()
    private var mListItemEvent: ArrayList<EventDetailModel> = ArrayList()
    private var statusCodeGoogleApiAvailability: Int? = null
    private var dayOfYear: Int? = null
    private var monthOfYear: Int? = null
    private var yearOfYear: Int? = null
    private var dateTimeStamp: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mCalendarManager = CalendarManager(context)
        mSharePreferenceManager = SharePreferenceGoogleSignInManager(context)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialToolbar()
        initialCalendar()
        initialRecyclerViewCalendar()

        defaultMonth()
        requestEventGoogleCalendar()
        tv_hind_bt.setOnClickListener { hindView: View -> onClick(hindView) }
        im_calendar_today.setOnClickListener { todayView: View -> onClick(todayView) }

        tv_calendar_select_date.text = mCalendarManager.initialCalendar().get(Calendar.DATE).toString()
        tv_calendar_year.text = mCalendarManager.initialCalendar().get(Calendar.YEAR).toString()
        tv_title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tv_header_month.text = defaultMonth()
    }

    private fun initialRecyclerViewCalendar() {
        calendar_recycler_list_event.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        calendar_recycler_list_event.itemAnimator = DefaultItemAnimator()

        mListEventCalendarAdapter = ListEventCalendarAdapter(mListItemEvent)
        calendar_recycler_list_event.adapter = mListEventCalendarAdapter
    }

    private fun initialToolbar() {
        ((activity as AppCompatActivity)).setSupportActionBar(toolbar_calendar)
        ((activity as AppCompatActivity)).supportActionBar?.setDisplayShowHomeEnabled(true)
        ((activity as AppCompatActivity)).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ((activity as AppCompatActivity)).supportActionBar?.title = ""
    }

    private fun initialCalendar() {
        compat_calendar_view.displayOtherMonthDays(true)
        compat_calendar_view.setFirstDayOfWeek(Calendar.WEDNESDAY)
        compat_calendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            @SuppressLint("SetTextI18n")
            override fun onDayClick(dateClicked: Date?) {
                mListEventDateClick = compat_calendar_view.getEvents(dateClicked)
                mCalendarManager.initialCalendar().time = dateClicked
                if (mListEventDateClick.count() == 0) {
                    mCalendarManager.animationHeaderExpanded(calendar_bar_app, maxHeaderCalendar, calendar_bar_app.height)
                    mCalendarManager.animationCalendarExpanded(compat_calendar_view, maxCalendarHeight, compat_calendar_view.height)

                    tv_calendar_detail_event.text = ""
                    tv_calendar_time_ticket.text = ""

                    tv_calendar_year.text = mCalendarManager.initialCalendar().get(Calendar.YEAR).toString()
                    tv_calendar_select_date.text = mCalendarManager.initialCalendar().get(Calendar.DATE).toString()
                    tv_header_month.text = mCalendarManager.initialCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("th"))
                    tv_hind_bt.visibility = View.GONE

                    mListItemEvent.clear()
                    mListEventCalendarAdapter.notifyDataSetChanged()
                } else {
                    if (mListEventDateClick.count() > 1) {
                        dayOfYear = mCalendarManager.initialCalendar().get(Calendar.DATE)
                        monthOfYear = mCalendarManager.initialCalendar().get(Calendar.MONTH) + 1
                        yearOfYear = mCalendarManager.initialCalendar().get(Calendar.YEAR)
                        dateTimeStamp = "0$dayOfYear/0$monthOfYear/$yearOfYear"

                        tv_calendar_select_date.text = ""
                        tv_calendar_year.text = ""
                        tv_calendar_detail_event.text = ""
                        tv_calendar_time_ticket.text = ""
                        tv_header_month.text = "EventList " + yearOfYear
                        tv_hind_bt.visibility = View.VISIBLE
                        mListItemEvent.clear()
                        for ((title, timeEventStart, timeEventEnd, timeDayOfYear, timeMonthDate, timeDateEvent) in mListItemShow) {
                            if (dateTimeStamp == timeDateEvent) {
                                eventDetailModel = EventDetailModel(title, timeEventStart, timeEventEnd, timeDayOfYear, timeMonthDate, timeDateEvent)
                                mListItemEvent.add(eventDetailModel)
                                mListEventCalendarAdapter.notifyDataSetChanged()
                            }
                        }

                        mCalendarManager.animationHeaderCollapse(calendar_bar_app, minHeaderCalendar, calendar_bar_app.height)
                        mCalendarManager.animationCalendarCollapse(compat_calendar_view, minCalendarHeight, compat_calendar_view.height)
                    } else if (mListEventDateClick.count() == 1) {
                        Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
                        tv_header_month.text = mCalendarManager.initialCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("th"))
                        tv_calendar_select_date.text = mCalendarManager.initialCalendar().get(Calendar.DATE).toString()
                        tv_calendar_year.text = mCalendarManager.initialCalendar().get(Calendar.YEAR).toString()

                        for ((title, timeEvent, timeEventEnd, timeDayOfYear, timeMonthDate, timeDateEvent) in mListItemShow) {
                            if (dateTimeStamp == timeDateEvent) {
                                tv_calendar_detail_event.text = title
                                tv_calendar_time_ticket.text = "$timeEvent น. - $timeEventEnd น."
                            }
                        }
                        mCalendarManager.animationHeaderExpanded(calendar_bar_app, maxHeaderCalendar, calendar_bar_app.height)
                        mCalendarManager.animationCalendarExpanded(compat_calendar_view, maxCalendarHeight, compat_calendar_view.height)
                    }
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                mCalendarManager.initialCalendar().time = firstDayOfNewMonth

                tv_calendar_year.text = mCalendarManager.initialCalendar().get(Calendar.YEAR).toString()
                tv_header_month.text = mCalendarManager.initialCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("th"))
                tv_calendar_select_date.text = mCalendarManager.initialCalendar().get(Calendar.DATE).toString()

                mCalendarManager.animationHeaderExpanded(calendar_bar_app, maxHeaderCalendar, calendar_bar_app.height)
                mCalendarManager.animationCalendarExpanded(compat_calendar_view, maxCalendarHeight, compat_calendar_view.height)
            }
        })
        mCalendarManager.animationCalendarExpanded(compat_calendar_view, maxCalendarHeight, compat_calendar_view.height)
    }


    private fun defaultMonth(): String {
        monthDefault = mCalendarManager.initialCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        return monthDefault
    }


    private fun requestEventGoogleCalendar() {
        if (!mGoogleServiceApiAvailabilityEnable()) {
            statusCodeGoogleApiAvailability = mCalendarManager.initialGoogleApiAvailability().isGooglePlayServicesAvailable(context)
            if (mCalendarManager.initialGoogleApiAvailability().isUserResolvableError(statusCodeGoogleApiAvailability!!)) {
                mCalendarManager.onShowDialogAlertGoogleService("GoogleServiceAvailability", statusCodeGoogleApiAvailability.toString()).show()
            }
        } else if (mCalendarManager.initialGoogleAccountCredential().selectedAccountName == null) {
            if (mSharePreferenceManager.defaultSharePreferenceManager() != null) {
                mCalendarManager.initialGoogleAccountCredential().selectedAccountName = mSharePreferenceManager.defaultSharePreferenceManager()
                MakeRequestTask(mCalendarManager.initialGoogleAccountCredential()).execute()
            } else {
                startActivityForResult(mCalendarManager.initialGoogleAccountCredential().newChooseAccountIntent(), REQUEST_ACCOUNT)
            }
        } else {
            MakeRequestTask(mCalendarManager.initialGoogleAccountCredential()).execute()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_hind_bt -> {
                val dayOfYear = mCalendarManager.initialCalendar().get(Calendar.DATE)
                tv_header_month.text = mCalendarManager.initialCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("th"))
                tv_calendar_select_date.text = "$dayOfYear"
                mCalendarManager.animationHeaderExpanded(calendar_bar_app, maxHeaderCalendar, calendar_bar_app.height)
                mCalendarManager.animationCalendarExpanded(compat_calendar_view, maxCalendarHeight, compat_calendar_view.height)
            }

            R.id.im_calendar_today -> {
                mCalendarToday = Calendar.getInstance()
                mCalendarToday.timeZone = TimeZone.getDefault()

                mDateCurrent = Date(mCalendarToday.timeInMillis)
                tv_calendar_select_date.text = mCalendarToday.get(Calendar.DATE).toString()
                tv_header_month.text = mCalendarToday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("th"))
                compat_calendar_view.setCurrentDate(mDateCurrent)

                mCalendarManager.animationHeaderExpanded(calendar_bar_app, maxHeaderCalendar, calendar_bar_app.height)
                mCalendarManager.animationCalendarExpanded(compat_calendar_view, maxCalendarHeight, compat_calendar_view.height)
            }
        }
    }

    private fun mGoogleServiceApiAvailabilityEnable(): Boolean {
        return mCalendarManager.initialGoogleApiAvailability().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ACCOUNT -> {
                mCalendarManager.initialGoogleAccountCredential().selectedAccountName = data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                mSharePreferenceManager.sharePreferenceManager(data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME))
                requestEventGoogleCalendar()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return true

    }


    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment().apply { arguments = Bundle() }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class MakeRequestTask(mGoogleCredentialAccount: GoogleAccountCredential) : AsyncTask<Void, Void, List<Event>>() {
        private var mService: com.google.api.services.calendar.Calendar? = null
        private var mListError: Exception? = null
        private lateinit var mDateTimeNow: DateTime
        private lateinit var eventListString: ArrayList<String>
        private lateinit var mEvents: Events
        private lateinit var mEventCalendar: com.github.sundeepk.compactcalendarview.domain.Event
        private lateinit var mListEvent: List<Event>
        private lateinit var mItemEvent: EventDetailModel
        private lateinit var mDateMonth: Date

        private var mDateStart: Date? = null
        private var mDateEnd: Date? = null
        private var transport: HttpTransport = AndroidHttp.newCompatibleTransport()
        private var jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

        init {
            mService = com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, mGoogleCredentialAccount)
                    .setApplicationName("Google Calendar Android QuickStart")
                    .build()
        }

        override fun doInBackground(vararg p0: Void?): List<Event>? {
            return try {
                getDataFromApi()
            } catch (e: Exception) {
                mListError = e
                cancel(true)
                null
            }
        }

        private fun getDataFromApi(): List<Event> {
            mDateTimeNow = DateTime(System.currentTimeMillis())
            eventListString = ArrayList()
            mEvents = mService?.events()?.list("primary")
                    ?.setMaxResults(10)
                    ?.setTimeMin(mDateTimeNow)
                    ?.setOrderBy("startTime")
                    ?.setSingleEvents(true)
                    ?.execute()!!

            mListEvent = mEvents.items
            return mListEvent
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("AsyncTaskPre", "start")
        }

        override fun onPostExecute(result: List<Event>?) {
            super.onPostExecute(result)
            for (items in result!!) {
                //Todo: Convert Start Or End Time
                mSimpleDateFormat = SimpleDateFormat("HH.mm", Locale("th"))
                mSimpleDateFormatDateTime = SimpleDateFormat("dd/MM/yyyy", Locale("th"))
                mSimpleDateFormatNickNameDate = SimpleDateFormat("MMM", Locale("th"))
                mSimpleDateFormatDateOfYear = SimpleDateFormat("d", Locale("th"))

                mDateStart = Date(items.start.dateTime.value)
                mDateEnd = Date(items.end.dateTime.value)

                mDateMonth = Date(items.start.dateTime.value)
                if (items.start != null) {
                    mItemEvent = EventDetailModel(items.summary
                            , mSimpleDateFormat.format(mDateStart)
                            , mSimpleDateFormat.format(mDateEnd)
                            , mSimpleDateFormatDateOfYear.format(mDateMonth)
                            , mSimpleDateFormatNickNameDate.format(mDateMonth)
                            , mSimpleDateFormatDateTime.format(mDateMonth))
                    mListItemShow.add(mItemEvent)

                    //Todo: AddEvent To Calendar
                    mEventCalendar = mCalendarManager.addEvent(items.start.dateTime, items)
                    compat_calendar_view.addEvent(mEventCalendar, true)
                }
            }
        }

        override fun onCancelled() {
            super.onCancelled()
            if (mListError != null) {
                when (mListError) {
                    is GooglePlayServicesAvailabilityIOException -> {
                        Log.d("AsyncTaskError", "GooglePlayServicesAvailability")
                    }
                    is UserRecoverableAuthIOException -> {
                        Log.d("AsyncTaskError", "UserRecoverable")
                    }
                    else -> {
                        Log.d("AsyncTaskError", mListError.toString())
                    }
                }
            }
        }
    }

}
