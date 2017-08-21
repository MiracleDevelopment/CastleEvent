package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent


class RealTimeDatabaseDetailManager(context: Context, lifecycle: Lifecycle, eventId: String, listDetailEventFragment: ListDetailEventFragment) : LifecycleObserver {
    var mContext: Context? = null
    var mLifecycle: Lifecycle? = null
    var mEventId: String? = null
    var mapEventId: String? = null
    var mListDetailEventFragment: ListDetailEventFragment? = null
    var onItemListDataChange: LoadingDetailData? = null
    var itemListEvent: ItemListEvent? = null
        get() = field

    lateinit var mChildEvent: ChildEventListener
    lateinit var hasMapData: HashMap<*, *>

    var realTimeDataDetail: DatabaseReference = FirebaseDatabase.getInstance().reference
    var realTimeDataDetailRef: DatabaseReference = realTimeDataDetail.child("eventItem").child("eventContinue")

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

    fun onChildEvent(): ChildEventListener {
        mChildEvent = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(mContext, p0?.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                hasMapData = p0?.value as HashMap<*, *>
                mapEventId = hasMapData["eventId"].toString()
                if (mapEventId.equals(mEventId)) {
                    onItemListDataChange = mListDetailEventFragment as LoadingDetailData
                    itemListEvent = ItemListEvent(
                            hasMapData["eventId"].toString(),
                            hasMapData["eventName"].toString(),
                            hasMapData["eventCover"].toString(),
                            hasMapData["eventAdvertise"].toString(),
                            hasMapData["categoryName"].toString(),
                            hasMapData["accountBank"].toString(),
                            hasMapData["eventDescription"].toString(),
                            hasMapData["eventLocation"].toString(),
                            hasMapData["eventLogoCredit"].toString(),
                            hasMapData["eventMax"] as Long,
                            hasMapData["eventRest"] as Long,
                            hasMapData["eventStatus"] as Boolean,
                            hasMapData["eventTime"].toString()
                    )
                    onItemListDataChange?.onLoadingUpdateData(itemListEvent!!)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }

        }
        return mChildEvent
    }
}
