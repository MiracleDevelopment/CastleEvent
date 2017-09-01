package com.ipati.dev.castleevent.model.GoogleCalendar.CalendarFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.DateTime
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.CalendarScopes
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.utill.CustomHeightViewCollapse
import com.ipati.dev.castleevent.utill.CustomHeightViewCollapseCalendar
import com.ipati.dev.castleevent.utill.CustomHeightViewExpanded
import com.ipati.dev.castleevent.utill.CustomHeightViewExpandedCalendar
import java.util.*


class CalendarManager(context: Context) {
    private lateinit var mCustomHeightViewCollapse: CustomHeightViewCollapse
    private lateinit var mCustomHeightViewExpanded: CustomHeightViewExpanded
    private lateinit var mCustomHeightViewCalendarCollapse: CustomHeightViewCollapseCalendar
    private lateinit var mCustomHeightViewCalendarExpanded: CustomHeightViewExpandedCalendar
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

    fun addEvent(event: DateTime, msg: Any?): Event {
        mEvent = Event(ContextCompat.getColor(mContext, R.color.colorEvent), event.value, msg)
        return mEvent
    }

    @Suppress("MemberVisibilityCanPrivate")
    fun convertHeightToDp(heightUser: Int): Int {
        return (heightUser * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun animationHeaderCollapse(view: View, heightUser: Int, heightView: Int): CustomHeightViewCollapse {
        mCustomHeightViewCollapse = CustomHeightViewCollapse(view, convertHeightToDp(heightUser), heightView)
        mCustomHeightViewCollapse.interpolator = AccelerateInterpolator()
        mCustomHeightViewCollapse.duration = 600
        view.animation = mCustomHeightViewCollapse
        view.startAnimation(mCustomHeightViewCollapse)
        return mCustomHeightViewCollapse
    }

    fun animationHeaderExpanded(view: View, heightUser: Int, heightView: Int): CustomHeightViewExpanded {
        mCustomHeightViewExpanded = CustomHeightViewExpanded(view, convertHeightToDp(heightUser), heightView)
        mCustomHeightViewExpanded.interpolator = AccelerateInterpolator()
        mCustomHeightViewExpanded.duration = 600
        view.animation = mCustomHeightViewExpanded
        view.startAnimation(mCustomHeightViewExpanded)
        return mCustomHeightViewExpanded
    }

    fun animationCalendarCollapse(view: View, heightUser: Int, heightView: Int): CustomHeightViewCollapseCalendar {
        mCustomHeightViewCalendarCollapse = CustomHeightViewCollapseCalendar(view, convertHeightToDp(heightUser), heightView)
        mCustomHeightViewCalendarCollapse.interpolator = AccelerateInterpolator()
        mCustomHeightViewCalendarCollapse.duration = 600
        view.animation = mCustomHeightViewCalendarCollapse
        view.startAnimation(mCustomHeightViewCalendarCollapse)
        return mCustomHeightViewCalendarCollapse
    }

    fun animationCalendarExpanded(view: View, heightUser: Int, heightView: Int): CustomHeightViewExpandedCalendar {
        mCustomHeightViewCalendarExpanded = CustomHeightViewExpandedCalendar(view, convertHeightToDp(heightUser), heightView)
        mCustomHeightViewCalendarExpanded.interpolator = AccelerateInterpolator()
        mCustomHeightViewCalendarExpanded.duration = 600
        view.animation = mCustomHeightViewCalendarExpanded
        view.startAnimation(mCustomHeightViewCalendarExpanded)
        return mCustomHeightViewCalendarExpanded
    }
}