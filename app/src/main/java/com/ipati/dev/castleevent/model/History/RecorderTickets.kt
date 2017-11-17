package com.ipati.dev.castleevent.model.History


data class RecorderTickets(var eventTickets: String = ""
                           , var userAccount: String = ""
                           , var eventId: String = ""
                           , var eventName: String = ""
                           , var eventLocation: String = ""
                           , var eventLogo: String = ""
                           , var eventCalendarId: String = ""
                           , var count: Long = 0
                           , var dateStamp: String = ""
                           , var timeStamp: Long = 0)
