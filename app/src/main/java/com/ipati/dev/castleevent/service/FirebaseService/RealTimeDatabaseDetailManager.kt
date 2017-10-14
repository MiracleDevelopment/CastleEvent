package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.OnUpdateInformation
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent


class RealTimeDatabaseDetailManager(context: Context, lifecycle: Lifecycle, eventId: Long, listDetailEventFragment: ListDetailEventFragment) : LifecycleObserver {
    private var mContext: Context? = null
    private var mLifecycle: Lifecycle? = null
    private var mEventId: Long? = null
    var mListDetailEventFragment: ListDetailEventFragment? = null
    var onItemListDataChange: LoadingDetailData? = null
    var onItemUpdateDataChange: OnUpdateInformation? = null

    lateinit var mChildEvent: ChildEventListener
    private var realTimeDataDetail: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var realTimeDataDetailRef: DatabaseReference = realTimeDataDetail.child("eventItem")
            .child("eventContinue")

    init {
        mEventId = eventId
        mContext = context
        mLifecycle = lifecycle
        mListDetailEventFragment = listDetailEventFragment
        mLifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        realTimeDataDetailRef.addChildEventListener(onChildEvent())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        realTimeDataDetailRef.removeEventListener(mChildEvent)
    }

    private fun onChildEvent(): ChildEventListener {
        mChildEvent = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
//                Log.d("onCancelled", p0?.message.toString())
                Toast.makeText(mContext, p0?.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val mItemListEvent: ItemListEvent? = p0?.getValue(ItemListEvent::class.java)
                mItemListEvent?.let {
                    onItemUpdateDataChange = mListDetailEventFragment as OnUpdateInformation
                    onItemUpdateDataChange?.setDataChange(mItemListEvent)

                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val mItemListEvent: ItemListEvent? = p0.getValue(ItemListEvent::class.java)
                mItemListEvent?.let {
                    if (mItemListEvent.eventId == mEventId) {
                        onItemListDataChange = mListDetailEventFragment as LoadingDetailData
                        onItemListDataChange?.onLoadingUpdateData(mItemListEvent)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        }
        return mChildEvent
    }
}
