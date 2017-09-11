package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListMyOrderAdapter
import com.ipati.dev.castleevent.model.history.RecorderTickets
import com.ipati.dev.castleevent.model.userManage.uid


class MyOrderRealTimeManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    var mContext: Context = context
    var Ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    var mRef: DatabaseReference = Ref.child("eventUser").child(uid)
    var mLifecycle: Lifecycle? = null

    var mListOrder: ArrayList<RecorderTickets> = ArrayList()
    var mAdapterMyOrder: ListMyOrderAdapter = ListMyOrderAdapter(mListOrder)

    lateinit var mRefChild: DatabaseReference
    lateinit var mHasMap: HashMap<*, *>
    lateinit var mChildEvent: ChildEventListener
    lateinit var mChildValueEvent: ChildEventListener

    init {
        mLifecycle = lifecycle
        mLifecycle!!.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        mRef.addChildEventListener(onChildMyOrderListener())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        mListOrder.clear()
        mRef.removeEventListener(mChildEvent)
    }

    private fun onChildMyOrderListener(): ChildEventListener {
        mChildEvent = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(mContext, p0?.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                mRefChild = Ref.child("eventUser").child(uid).child(p0?.key.toString())
                mRefChild.addChildEventListener(onChildValueListener())
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        }
        return mChildEvent
    }

    private fun onChildValueListener(): ChildEventListener {
        mChildValueEvent = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(mContext, p0?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                mHasMap = p0?.value as HashMap<*, *>
                val recorderTickets = RecorderTickets(mHasMap["userAccount"].toString()
                        , mHasMap["eventId"].toString()
                        , mHasMap["eventName"].toString()
                        , mHasMap["eventLocation"].toString()
                        , mHasMap["eventLogo"].toString()
                        , mHasMap["count"] as Long
                        , mHasMap["dateStamp"].toString()
                        , mHasMap["timeStamp"] as Long)

                mListOrder.add(recorderTickets)
                mAdapterMyOrder.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }

        }
        return mChildValueEvent
    }
}