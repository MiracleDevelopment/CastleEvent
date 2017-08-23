package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListEventMenuAdapter


class RealTimeDatabaseMenuListItem(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    private var Ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mRef: DatabaseReference = Ref.child("eventMenu")
    var listItemMenu: ArrayList<String> = ArrayList()
    var adapterListItemMenu: ListEventMenuAdapter = ListEventMenuAdapter(listItemMenu)

    private var mContext: Context? = null
    private var mLifeCycle: Lifecycle? = null

    init {
        this.mContext = context
        this.mLifeCycle = lifecycle
        mLifeCycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun addListItemMenu() {
        mRef.addChildEventListener(childListItem())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun removeListItemMenu() {
        listItemMenu.clear()
        mRef.removeEventListener(childListItem())
    }

    private fun childListItem(): ChildEventListener {
        return object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(mContext, p0?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                listItemMenu.add(p0?.getValue(String::class.java)!!)
                adapterListItemMenu.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }

        }
    }
}