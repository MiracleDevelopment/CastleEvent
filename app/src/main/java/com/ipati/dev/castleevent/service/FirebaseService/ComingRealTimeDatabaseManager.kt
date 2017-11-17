package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ComingListEventAdapter
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent


class ComingRealTimeDatabaseManager(lifecycle: Lifecycle) : LifecycleObserver {
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val refDatabase: DatabaseReference? = ref.child(eventObject).child(eventContinue).child("coming")
    private val lifeCycle: Lifecycle = lifecycle

    var listItemEventComing: ArrayList<ItemListEvent> = ArrayList()
    var adapterListComing: ComingListEventAdapter = ComingListEventAdapter(listItemEventComing)

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
            listItemEventComing.clear()
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
            val itemListEvent: ItemListEvent? = p0?.getValue(ItemListEvent::class.java)
            itemListEvent?.let {
                val itemListChange: ItemListEvent? = listItemEventComing.find { it.eventKey == p0.key }
                val indexChange: Int = listItemEventComing.indexOf(itemListChange)
                listItemEventComing[indexChange] = itemListEvent
                adapterListComing.notifyItemChanged(indexChange)
            }
        }

        override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            val itemListEvent: ItemListEvent? = p0?.getValue(ItemListEvent::class.java)
            itemListEvent?.let {
                listItemEventComing.add(itemListEvent)
                adapterListComing.notifyDataSetChanged()
            }
        }

        override fun onChildRemoved(p0: DataSnapshot?) {

        }
    }

    companion object {
        private const val eventObject: String = "eventItem"
        private const val eventContinue: String = "eventContinue"
    }
}