package com.ipati.dev.castleevent.model.modelListEvent

import java.lang.reflect.Constructor

data class ItemListEvent(var updateKey: String = ""
                         , var eventId: Long = 0
                         , var eventName: String = ""
                         , var eventCover: String = ""
                         , var eventAdvertise: String = ""
                         , var categoryName: String = ""
                         , var accountBank: String = ""
                         , var eventDescription: String = ""
                         , var eventLocation: String = ""
                         , var eventLogoCredit: String = ""
                         , var eventLatitude: Double = 0.0
                         , var eventLongitude: Double = 0.0
                         , var eventMax: Long = 0
                         , var eventRest: Long = 0
                         , var eventStatus: Boolean = false
                         , var eventTime: String = ""
                         , var eventCalendarStart: String = ""
                         , var eventCalendarEnd: String = ""
                         , var eventPrice: String = "")






