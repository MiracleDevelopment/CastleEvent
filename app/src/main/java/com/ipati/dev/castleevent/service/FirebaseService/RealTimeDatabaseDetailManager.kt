package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.OnUpdateInformation
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent


class RealTimeDatabaseDetailManager(context: Context, lifecycle: Lifecycle, eventId: Long, listDetailEventFragment: ListDetailEventFragment) : LifecycleObserver {
    private var contextManager: Context? = null
    private var lifecycleManager: Lifecycle? = null
    private var eventIdManager: Long? = null

    var listDetailEventFragmentManager: ListDetailEventFragment? = null
    var onItemListDataChange: LoadingDetailData? = null
    var onItemUpdateDataChange: OnUpdateInformation? = null

    private lateinit var childEvent: ChildEventListener
    private var realTimeDataDetail: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var realTimeDataDetailRef: DatabaseReference = realTimeDataDetail.child("eventItem")
            .child("eventContinue").child("news")

    init {
        eventIdManager = eventId
        contextManager = context
        lifecycleManager = lifecycle
        listDetailEventFragmentManager = listDetailEventFragment
        lifecycleManager?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        realTimeDataDetailRef.addChildEventListener(onChildEvent())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        realTimeDataDetailRef.removeEventListener(childEvent)
    }

    private fun onChildEvent(): ChildEventListener {
        childEvent = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d("onCancelledDetailEvent", p0?.message.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val itemListEvent: ItemListEvent? = p0?.getValue(ItemListEvent::class.java)
                itemListEvent?.let {
                    onItemUpdateDataChange = listDetailEventFragmentManager as OnUpdateInformation
                    onItemUpdateDataChange?.setDataChange(itemListEvent)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val itemListEvent: ItemListEvent? = p0.getValue(ItemListEvent::class.java)
                itemListEvent?.let {
                    if (eventIdManager == itemListEvent.eventId) {
                        onItemListDataChange = listDetailEventFragmentManager as LoadingDetailData
                        onItemListDataChange?.onLoadingUpdateData(itemListEvent)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        }
        return childEvent
    }
}
