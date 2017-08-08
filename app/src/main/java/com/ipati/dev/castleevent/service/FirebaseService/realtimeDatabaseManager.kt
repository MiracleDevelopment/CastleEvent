package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListEventAdapter
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent

class realTimeDatabaseManager(context: Context, lifeCycle: Lifecycle) : LifecycleObserver {
    var tagChild = "eventItem/eventContinue"
    var refDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    var refDatabaseChild: DatabaseReference = refDatabase.child(tagChild)
    var listItem: ItemListEvent? = null
    var hasMapData: HashMap<*, *>? = null
    var mContext: Context? = null
    var mLifeCycle: Lifecycle? = null
    var arrayItemList: ArrayList<ItemListEvent> = ArrayList()
    var adapterListEvent: ListEventAdapter? = ListEventAdapter(listItem = arrayItemList)
        get() = field


    //Todo:init Class Constructor
    init {
        this.mContext = context
        this.mLifeCycle = lifeCycle
        mLifeCycle!!.addObserver(this)
    }

    //Todo:Handling Life Cycle
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Toast.makeText(mContext, "onStart", Toast.LENGTH_SHORT).show()
        registerChildEvent()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Toast.makeText(mContext, "onStop", Toast.LENGTH_SHORT).show()
        removeChildEvent()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mLifeCycle?.removeObserver(this)
    }

    fun registerChildEvent() {
        refDatabaseChild.addChildEventListener(childEventListener())
    }

    fun removeChildEvent() {
        refDatabaseChild.removeEventListener(childEventListener())
        arrayItemList.clear()
    }

    fun childEventListener(): ChildEventListener {
        val childEvent: ChildEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d("onCancelled", p0?.message.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                hasMapData = p0?.value as HashMap<*, *>
                val objectItem: List<ItemListEvent> = arrayItemList.filter { it.eventId == hasMapData!!["eventId"].toString() }
                listItem = ItemListEvent(
                        hasMapData!!["eventId"].toString(),
                        hasMapData!!["eventName"].toString(),
                        hasMapData!!["eventCover"].toString(),
                        hasMapData!!["categoryName"].toString(),
                        hasMapData!!["accountBank"].toString(),
                        hasMapData!!["eventDescription"].toString(),
                        hasMapData!!["eventLocation"].toString(),
                        hasMapData!!["eventMax"] as Long,
                        hasMapData!!["eventRest"] as Long,
                        hasMapData!!["eventStatus"] as Boolean,
                        hasMapData!!["eventTime"].toString()
                )
                arrayItemList.remove(objectItem[0])
                adapterListEvent?.notifyDataSetChanged()

                arrayItemList.add(0, listItem!!)

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                hasMapData = p0?.value as HashMap<*, *>?
                listItem = ItemListEvent(
                        hasMapData!!["eventId"].toString(),
                        hasMapData!!["eventName"].toString(),
                        hasMapData!!["eventCover"].toString(),
                        hasMapData!!["categoryName"].toString(),
                        hasMapData!!["accountBank"].toString(),
                        hasMapData!!["eventDescription"].toString(),
                        hasMapData!!["eventLocation"].toString(),
                        hasMapData!!["eventMax"] as Long,
                        hasMapData!!["eventRest"] as Long,
                        hasMapData!!["eventStatus"] as Boolean,
                        hasMapData!!["eventTime"].toString()
                )

                Log.d("somethingValue", listItem?.eventName)
                arrayItemList.add(listItem!!)
                adapterListEvent?.notifyDataSetChanged()

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                hasMapData = p0?.value as HashMap<*, *>
                val objectItem: List<ItemListEvent> = arrayItemList.filter { it.eventId == hasMapData!!["eventId"].toString() }
                adapterListEvent?.notifyDataSetChanged()
                arrayItemList.remove(objectItem[0])
            }
        }
        return childEvent
    }


}
