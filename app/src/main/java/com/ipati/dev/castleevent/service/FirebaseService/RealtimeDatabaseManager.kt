package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListEventAdapter
import com.ipati.dev.castleevent.extension.onShowToast
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent

class RealTimeDatabaseManager(context: Context, lifeCycle: Lifecycle) : LifecycleObserver {
    private var contextManager: Context = context
    private var lifeCycleManager: Lifecycle? = null
    private var itemListEvent: ItemListEvent? = null
    private var onChildListener: ChildEventListener? = null
    var arrayItemList: ArrayList<ItemListEvent> = ArrayList()
    var adapterListEvent: ListEventAdapter? = ListEventAdapter(listItem = arrayItemList)

    private var tagChild = "eventItem/eventContinue"
    private var refDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var refDatabaseChild: DatabaseReference? = refDatabase.child(tagChild).child("news")

    //Todo:init Class Constructor
    init {
        lifeCycleManager = lifeCycle
        lifeCycleManager!!.addObserver(this)
    }

    //Todo:Handling Life Cycle
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        registerChildEvent()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        removeChildEvent()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lifeCycleManager?.removeObserver(this)
    }

    private fun registerChildEvent() {
        refDatabaseChild?.addChildEventListener(childEventListener())
    }

    private fun removeChildEvent() {
        refDatabaseChild?.let {
            refDatabaseChild?.removeEventListener(onChildListener)
        }

        arrayItemList.clear()
    }


    private fun childEventListener(): ChildEventListener? {
        onChildListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d("onCancelledListEvent", p0?.message.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                itemListEvent = p0?.getValue(ItemListEvent::class.java)
                itemListEvent?.let {
                    val itemChange: ItemListEvent? = arrayItemList.find { it.eventKey == itemListEvent?.eventKey }
                    val indexRef: Int = arrayItemList.indexOf(itemChange)
                    arrayItemList[indexRef] = itemListEvent!!
                    adapterListEvent?.notifyItemChanged(indexRef)
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                itemListEvent = p0?.getValue(ItemListEvent::class.java)
                itemListEvent?.let {
                    arrayItemList.add(itemListEvent!!)
                    adapterListEvent?.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                itemListEvent = p0?.getValue(ItemListEvent::class.java)
                itemListEvent?.let {
                    val itemRemove: ItemListEvent? = arrayItemList.find { it.eventKey == itemListEvent?.eventKey }
                    val indexRef: Int = arrayItemList.indexOf(itemRemove)
                    arrayItemList.removeAt(indexRef)
                    adapterListEvent?.notifyItemRemoved(indexRef)
                }
            }
        }

        return onChildListener
    }
}
