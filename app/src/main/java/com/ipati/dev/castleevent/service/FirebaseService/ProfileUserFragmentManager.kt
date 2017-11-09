package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.firebase.database.*
import com.ipati.dev.castleevent.model.UserManager.ExtendedProfileUserModel
import com.ipati.dev.castleevent.model.UserManager.uid

class ProfileUserFragmentManager(lifecycle: Lifecycle) : LifecycleObserver {
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val refProfile: DatabaseReference? = ref.child("userProfile").child(uid)
    private val lifecycleManager: Lifecycle = lifecycle

    var callBackManager: ((key: String, userProfile: ExtendedProfileUserModel?) -> Unit?)? = null

    init {
        lifecycleManager.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        refProfile?.addChildEventListener(addOnChildEventListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        refProfile?.let {
            refProfile.removeEventListener(addOnChildEventListener)
        }
    }

    private val addOnChildEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError?) {

        }

        override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

        }

        override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

        }

        override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            val modelProfileEvent: ExtendedProfileUserModel? = p0?.getValue(ExtendedProfileUserModel::class.java)
            modelProfileEvent?.let {
                callBackManager?.invoke(p0.key, modelProfileEvent)
            }
        }

        override fun onChildRemoved(p0: DataSnapshot?) {

        }
    }

}