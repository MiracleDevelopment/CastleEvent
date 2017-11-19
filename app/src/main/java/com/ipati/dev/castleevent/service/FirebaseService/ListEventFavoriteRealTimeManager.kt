package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListEventFavoriteAdapter
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent


class ListEventFavoriteRealTimeManager(msg: String, lifecycle: Lifecycle) : LifecycleObserver {
    private val lifeCycleManager: Lifecycle = lifecycle
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val refDatabase: DatabaseReference? = ref.child("eventItem").child("eventContinue").child("news")
    val listItemFavoriteEvent: ArrayList<ItemListEvent> = ArrayList()
    val adapterListFavoriteItem: ListEventFavoriteAdapter = ListEventFavoriteAdapter(listItemFavoriteEvent)

    init {
        lifeCycleManager.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        refDatabase?.addChildEventListener(childEventListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        refDatabase?.let {
            refDatabase.removeEventListener(childEventListener)
        }
    }


    private val childEventListener: ChildEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError?) {
            Log.d("onCancelledListFavorite", p0?.message.toString())
        }

        override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

        }

        override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

        }

        override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            val itemListEvent: ItemListEvent? = p0?.getValue(ItemListEvent::class.java)
            itemListEvent?.let {
                listItemFavoriteEvent.add(itemListEvent)
                adapterListFavoriteItem.notifyDataSetChanged()
            }
        }

        override fun onChildRemoved(p0: DataSnapshot?) {

        }
    }
}