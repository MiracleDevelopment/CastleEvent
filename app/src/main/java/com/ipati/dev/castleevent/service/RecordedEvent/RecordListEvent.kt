package com.ipati.dev.castleevent.service.RecordedEvent

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ipati.dev.castleevent.model.GoogleCalendar.idEvent
import com.ipati.dev.castleevent.model.history.RecorderTickets
import com.ipati.dev.castleevent.model.userManage.uid


class RecordListEvent(context: Context) {
    private var Ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mRef: DatabaseReference = Ref.child("eventUser").child(uid).child(idEvent)
    private lateinit var mRecorderTickets: RecorderTickets

    fun pushEventRealTime(userName: String, eventId: String, eventName: String, locationEvent: String, logoEvent: String, count: Long, dateStamp: String, timeStamp: Long): Task<Void>? {
        mRecorderTickets = RecorderTickets(userName, eventId, eventName, locationEvent, logoEvent, count, dateStamp, timeStamp)
        return mRef.push().setValue(mRecorderTickets)
    }
}