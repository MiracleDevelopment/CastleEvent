package com.ipati.dev.castleevent.fragment

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.ipati.dev.castleevent.model.EventDetailModel
import com.ipati.dev.castleevent.model.GoogleCalendar.CalendarFragment.CalendarManager
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import kotlinx.android.synthetic.main.activity_calendar_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {
    private var REQUEST_ACCOUNT: Int = 1111
    private lateinit var mCalendarManager: CalendarManager
    private lateinit var mSharePreferenceManager: SharePreferenceGoogleSignInManager
    private lateinit var monthDefault: String
    private lateinit var monthScroll: String
    private lateinit var dateScroll: String
    private lateinit var mListEventDateClick: List<com.github.sundeepk.compactcalendarview.domain.Event>
    private lateinit var mSimpleDateFormat: SimpleDateFormat
    private var mCalender: Calendar = Calendar.getInstance()
    private var mListItemShow: ArrayList<EventDetailModel> = ArrayList()
    private var statusCodeGoogleApiAvailability: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCalendarManager = CalendarManager(context)
        mSharePreferenceManager = SharePreferenceGoogleSignInManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialCalendar()
        defaultMonth()

        tv_header_month.text = defaultMonth()
        tv_calendar_select_date.text = mCalendarManager.initialCalendar().get(Calendar.DATE).toString()
        addEvent()
        requestEventGoogleCalendar()
    }

    private fun initialCalendar() {
        compat_calendar_view.displayOtherMonthDays(true)
        compat_calendar_view.setFirstDayOfWeek(Calendar.WEDNESDAY)
        compat_calendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            @SuppressLint("SetTextI18n")
            override fun onDayClick(dateClicked: Date?) {
                mListEventDateClick = compat_calendar_view.getEvents(dateClicked)
                if (mListEventDateClick.count() == 0) {
                    tv_calendar_detail_event.text = " "
                    tv_calendar_time_ticket.text = " "
                } else {
                    for ((title, timeEventStart, timeEventEnd) in mListItemShow) {
                        tv_calendar_detail_event.text = title
                        tv_calendar_time_ticket.text = "เวลา $timeEventStart น. - $timeEventEnd น."
                    }
                }

                mCalendarManager.initialCalendar().time = dateClicked
                tv_calendar_select_date.text = mCalendarManager.initialCalendar().get(Calendar.DATE).toString()
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                mCalendarManager.initialCalendar().time = firstDayOfNewMonth
                monthScroll = mCalendarManager.initialCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                dateScroll = mCalendarManager.initialCalendar().get(Calendar.DATE).toString()
                tv_header_month.text = monthScroll
                tv_calendar_select_date.text = dateScroll
            }
        })
    }


    private fun defaultMonth(): String {
        monthDefault = mCalendarManager.initialCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        return monthDefault
    }

    private fun addEvent() {
        mCalendarManager.initialCalendar().set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND)
        compat_calendar_view.addEvent(mCalendarManager.addEvent("Thailand Best Shopping Fair 2017"), true)
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
                MakeRequestTask(mGoogleCredentialAccount = mCalendarManager.initialGoogleAccountCredential()).execute()
            } else {
                startActivityForResult(mCalendarManager.initialGoogleAccountCredential().newChooseAccountIntent(), REQUEST_ACCOUNT)
            }
        } else {
            MakeRequestTask(mGoogleCredentialAccount = mCalendarManager.initialGoogleAccountCredential()).execute()
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
        private lateinit var mDateFormatStart: String
        private lateinit var mDateFormatEnd: String
        private lateinit var mDateStart: Date
        private lateinit var mDateEnd: Date

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
            Toast.makeText(context, "Read Event", Toast.LENGTH_SHORT).show()
            for (items in result!!) {
                //Todo: Convert Start Or End Time
                mSimpleDateFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
                mDateStart = Date(items.start.dateTime.value)
                mDateFormatStart = mSimpleDateFormat.format(mDateStart)

                mDateEnd = Date(items.end.dateTime.value)
                mDateFormatEnd = mSimpleDateFormat.format(mDateEnd)

                mItemEvent = EventDetailModel(items.summary, mDateFormatStart, mDateFormatEnd)
                mListItemShow.add(mItemEvent)


                //Todo: AddEvent To Calendar
                mEventCalendar = com.github.sundeepk.compactcalendarview.domain
                        .Event(ContextCompat.getColor(context, R.color.colorEvent)
                                , items.start.dateTime.value, items.summary)

                compat_calendar_view.addEvent(mEventCalendar, true)
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
