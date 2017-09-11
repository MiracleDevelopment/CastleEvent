package com.ipati.dev.castleevent.model.history


data class RecorderTickets(var userAccount: String = ""
                           , var eventId: String = ""
                           , var eventName: String = ""
                           , var eventLocation: String = ""
                           , var eventLogo: String = ""
                           , var count: Long = 0
                           , var dateStamp: String = ""
                           , var timeStamp: Long = 0)
