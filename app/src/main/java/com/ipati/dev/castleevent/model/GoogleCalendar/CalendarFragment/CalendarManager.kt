package com.ipati.dev.castleevent.model.GoogleCalendar.CalendarFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.v4.content.ContextCompat
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.CalendarScopes
import com.ipati.dev.castleevent.R
import java.util.*


class CalendarManager(context: Context) {
    private lateinit var mAlertDialog: AlertDialog.Builder
    private lateinit var mDialog: Dialog
    private lateinit var mEvent: Event

    private var mContext: Context = context
    private var mCalendar: Calendar = Calendar.getInstance()
    private var mGoogleAccountCredential: GoogleAccountCredential
    private var mGoogleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()

    init {
        mGoogleAccountCredential = GoogleAccountCredential.usingOAuth2(mContext,
                Arrays.asList(CalendarScopes.CALENDAR))
                .setBackOff(ExponentialBackOff())
    }

    fun initialCalendar(): Calendar {
        mCalendar.timeZone = TimeZone.getDefault()
        return mCalendar
    }

    fun initialGoogleApiAvailability(): GoogleApiAvailability {
        return mGoogleApiAvailability
    }

    fun initialGoogleAccountCredential(): GoogleAccountCredential {
        return mGoogleAccountCredential
    }

    fun onShowDialogAlertGoogleService(title: String, msg: String): Dialog {
        mAlertDialog = AlertDialog.Builder(mContext)
        mAlertDialog.setCancelable(false)
        mAlertDialog.setTitle(title)
        mAlertDialog.setMessage(msg)

        mDialog = mAlertDialog.create()
        return mDialog
    }


    fun addEvent(msg: String): Event {
        mEvent = Event(ContextCompat.getColor(mContext, R.color.colorEvent), mCalendar.timeInMillis, msg)
        return mEvent
    }
}