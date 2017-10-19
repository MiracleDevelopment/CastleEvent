package com.ipati.dev.castleevent.model.Date

import android.content.Context
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.GoogleCalendar.restEvent
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import com.ipati.dev.castleevent.service.AuthenticationStatus
import java.text.SimpleDateFormat
import java.util.*


class DateManager(context: Context) {
    private var contextManager: Context = context

    private val authenticationStatus: AuthenticationStatus by lazy {
        AuthenticationStatus()
    }

    private val simpleDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale(getLocalDefault()))
    }

    private val calendarInstance: Calendar by lazy {
        Calendar.getInstance()
    }

    private fun getLocalDefault(): String {
        return "th"
    }

    fun convertStringDate(dateString: String?): String? {
        return getDateCalendar(date = simpleDateFormat.parse(dateString))
    }

    private fun getDateCalendar(date: Date): String {
        calendarInstance.timeInMillis = date.time
        return "${calendarInstance.get(Calendar.DAY_OF_MONTH)} ${calendarInstance.getDisplayName(Calendar.MONTH
                , Calendar.LONG, Locale(getLocalDefault()))} ${calendarInstance.get(Calendar.YEAR)}"
    }

    fun getStatusTickets(itemListEvent: ItemListEvent, setTextStatus: (String) -> Unit) {
        if (authenticationStatus.getCurrentUser() != null) {
            if (Date().before(simpleDateFormat.parse(itemListEvent.eventCalendarStart))) {
                if (restEvent!! > 0) {
                    setTextStatus("${itemListEvent.eventPrice}/Tickets")
                } else {
                    setTextStatus(contextManager.resources.getString(R.string.expiredTickets))
                }
            } else if (Date().after(simpleDateFormat.parse(itemListEvent.eventCalendarEnd))) {
                setTextStatus(contextManager.resources.getString(R.string.lessAfterDate))
            } else {
                setTextStatus(contextManager.resources.getString(R.string.closeEvent))
            }
        } else {
            setTextStatus(contextManager.resources.getString(R.string.pleaseLogin))
        }
    }

    fun getCurrentDate(): String {
        return "${calendarInstance.get(Calendar.DAY_OF_MONTH)}-" +
                "${calendarInstance.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale(getLocalDefault()))}-" +
                "${calendarInstance.get(Calendar.YEAR)}"
    }

}