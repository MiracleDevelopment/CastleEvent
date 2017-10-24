package com.ipati.dev.castleevent.service.RecordedEvent

import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.ipati.dev.castleevent.model.GoogleCalendar.*
import com.ipati.dev.castleevent.model.History.RecorderTickets
import com.ipati.dev.castleevent.model.UserManager.uid

class RecordListEvent {
    private var ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var refListEvent: DatabaseReference
    private lateinit var recorderTickets: RecorderTickets

    fun pushEventRealTime(userName: String?, eventId: String?
                          , eventName: String?, locationEvent: String?, logoEvent: String?
                          , count: Long, dateStamp: String, timeStamp: Long): Task<Void>? {

        refListEvent = ref.child("eventUser").child(uid).child(idEvent)
        recorderTickets = RecorderTickets(userName!!, eventId!!, eventName!!, locationEvent!!, logoEvent!!, count, dateStamp, timeStamp)
        return refListEvent.push().setValue(recorderTickets)
    }
}