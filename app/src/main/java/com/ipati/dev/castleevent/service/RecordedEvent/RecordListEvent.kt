package com.ipati.dev.castleevent.service.RecordedEvent

import android.util.Log
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
                          , count: Long, dateStamp: String, timeStamp: Long, fireBaseCallBack: ((id: String) -> Unit)): Task<Void>? {
        refListEvent = ref.child("eventUser").child(uid).push()

        recorderTickets = RecorderTickets(refListEvent.push().key
                , userName!!, eventId!!, eventName!!
                , locationEvent!!, logoEvent!!, "", count
                , dateStamp, timeStamp)
        fireBaseCallBack(refListEvent.key)
        return refListEvent.setValue(recorderTickets)
    }
}