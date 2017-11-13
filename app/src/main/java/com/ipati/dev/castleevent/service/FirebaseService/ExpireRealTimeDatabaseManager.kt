package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ExpireListEventAdapter
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent


class ExpireRealTimeDatabaseManager(lifecycle: Lifecycle) : LifecycleObserver {
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val refDatabase: DatabaseReference? = ref.child(eventItemObject).child(eventContinue).child("expire")
    private val lifeCycle: Lifecycle = lifecycle

    private var listItemEventExpire: ArrayList<ItemListEvent> = ArrayList()
    var adapterExpire: ExpireListEventAdapter = ExpireListEventAdapter(listItemEventExpire)

    init {
        lifeCycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        refDatabase?.addChildEventListener(childEventListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        refDatabase?.let {
            listItemEventExpire.clear()
            refDatabase.removeEventListener(childEventListener)
        }
    }

    private val childEventListener: ChildEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError?) {
            Log.d("onCancelled", p0?.message.toString())
        }

        override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

        }

        override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

        }

        override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            val itemListEvent: ItemListEvent? = p0?.getValue(ItemListEvent::class.java)
            itemListEvent?.let {
                listItemEventExpire.add(itemListEvent)
            }
            adapterExpire.notifyDataSetChanged()
        }

        override fun onChildRemoved(p0: DataSnapshot?) {

        }
    }

    companion object {
        private const val eventItemObject: String = "eventItem"
        private const val eventContinue: String = "eventContinue"
    }
}