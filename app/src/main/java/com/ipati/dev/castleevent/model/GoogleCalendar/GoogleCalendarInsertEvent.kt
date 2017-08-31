package com.ipati.dev.castleevent.model.GoogleCalendar


import android.util.Log
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import com.google.api.services.calendar.model.EventReminder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GoogleCalendarInsertEvent(summary: String?, location: String?, description: String?) {
    private lateinit var startDateTime: DateTime
    private lateinit var endDateTime: DateTime
    private lateinit var eventDateTimeStart: EventDateTime
    private lateinit var eventDateTimeEnd: EventDateTime
    private lateinit var listRecurrence: List<String>
    private lateinit var listEventAttendee: List<EventAttendee>
    private lateinit var listEventReminder: List<EventReminder>
    private lateinit var reminders: Event.Reminders
    private lateinit var mSimpleDateFormat: SimpleDateFormat
    private lateinit var mDate: Date
    private lateinit var simpleDateStart: String
    private lateinit var simpleDateEnd: String
    var event: Event = Event()
    var mCalendar: Calendar = Calendar.getInstance()
    var mTimeZone: TimeZone = mCalendar.timeZone

    init {
        event.summary = summary
        event.location = location
        event.description = description
        event.start = setDateTimeStart()
        event.end = setDateTimeEnd()
        event.recurrence = setRecurrence()
        event.attendees = setEventAttendee()
        event.reminders = setEventReminder()
    }

    private fun setDateTimeStart(): EventDateTime {
        simpleDateStart = "01-9-2017T07:00:00-10:00"
        mSimpleDateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale("th"))
        mDate = mSimpleDateFormat.parse(simpleDateStart)
        Log.d("DateTimeFormatStart", mDate.toString())

        startDateTime = DateTime(mDate.time)
        eventDateTimeStart = EventDateTime().setDateTime(startDateTime).setTimeZone(mTimeZone.id.toString())
        return eventDateTimeStart
    }

    private fun setDateTimeEnd(): EventDateTime {
        simpleDateEnd = "01-9-2017T10:00:00-10:00"
        mSimpleDateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale("th"))
        mDate = mSimpleDateFormat.parse(simpleDateEnd)
        Log.d("DateTimeFormatEnd", mDate.toString())

        endDateTime = DateTime(mDate.time)
        eventDateTimeEnd = EventDateTime().setDateTime(endDateTime).setTimeZone(mTimeZone.id.toString())
        return eventDateTimeEnd
    }

    private fun setRecurrence(): List<String> {
        listRecurrence = ArrayList(Arrays.asList("RRULE:FREQ=DAILY;COUNT=2"))
        return listRecurrence
    }

    private fun setEventAttendee(): List<EventAttendee> {
        listEventAttendee = ArrayList(Arrays.asList(EventAttendee().setEmail(attendee)))
        return listEventAttendee
    }

    private fun setEventReminder(): Event.Reminders {
        reminders = Event.Reminders().setUseDefault(false).setOverrides(setEventListReminder())
        return reminders
    }

    private fun setEventListReminder(): List<EventReminder> {
        listEventReminder = ArrayList(Arrays.asList(EventReminder().setMethod("email").setMinutes(24 * 60)
                , EventReminder().setMethod("popup").setMinutes(24 * 60)))
        return listEventReminder
    }

    fun requestEvent(): Event? {
        return event
    }
}