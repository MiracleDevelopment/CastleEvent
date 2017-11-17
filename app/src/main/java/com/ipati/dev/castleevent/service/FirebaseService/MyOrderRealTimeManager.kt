package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListMyOrderAdapter
import com.ipati.dev.castleevent.model.History.RecorderTickets
import com.ipati.dev.castleevent.model.UserManager.uid


class MyOrderRealTimeManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    var contextManager: Context = context
    private var Ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val refMyOrder: DatabaseReference? = Ref.child("eventUser").child(uid)
    private var lifecycle: Lifecycle? = null

    var listOrder: ArrayList<RecorderTickets> = ArrayList()
    var adapterMyOrder: ListMyOrderAdapter = ListMyOrderAdapter(listOrder)

    lateinit var childEvent: ChildEventListener
    lateinit var childValueEvent: ChildEventListener

    init {
        this.lifecycle = lifecycle
        this.lifecycle!!.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        refMyOrder?.let {
            refMyOrder.addChildEventListener(onChildMyOrderListener())
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        refMyOrder?.let {
            refMyOrder.removeEventListener(childEvent)
            listOrder.clear()
        }
    }


    private fun onChildMyOrderListener(): ChildEventListener {
        childEvent = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(contextManager, p0?.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val mItemRecordTickets: RecorderTickets? = p0?.getValue(RecorderTickets::class.java)
                mItemRecordTickets?.let {
                    listOrder.add(mItemRecordTickets)
                    adapterMyOrder.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        }
        return childEvent
    }
}