package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.FavoriteMenuAdapter
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.uidRegister
import com.ipati.dev.castleevent.service.RecordedEvent.CategoryRecordData


class FavoriteCategoryRealTimeDatabaseManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    var contextManager: Context = context
    var listItemFavorite: ArrayList<CategoryRecordData> = ArrayList()
    var adapterFavorite: FavoriteMenuAdapter = FavoriteMenuAdapter(listItemFavorite)
    var onChangeItemCount: ((count: Int) -> Unit?)? = null

    private var lifecycleManager: Lifecycle? = lifecycle
    private var ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var refDatabase: DatabaseReference? = null
    private var onChildEventListener: ChildEventListener? = null
    private var onValueListener: ValueEventListener? = null

    init {
        lifecycleManager?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        ref.child(keyChild).addListenerForSingleValueEvent(setOnValueListener())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        ref.removeEventListener(onValueListener)
        refDatabase?.let {
            refDatabase?.removeEventListener(onChildEventListener)
        }
    }

    private fun setOnValueListener(): ValueEventListener? {
        onValueListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d("onCancelled", p0?.message.toString())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0?.exists()!!) {
                    if (p0.hasChild(uid)) {
                        onChangeItemCount?.invoke(1)
                        refDatabase = ref.child(keyChild).child(uid)
                        refDatabase?.addChildEventListener(setChildEvent())
                    } else {
                        onChangeItemCount?.invoke(0)
                    }
                } else {
                    onChangeItemCount?.invoke(0)
                }
            }
        }
        return onValueListener
    }


    private fun setChildEvent(): ChildEventListener? {
        onChildEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d("onCancelled", p0?.message.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val recordCategoryData = p0?.getValue(CategoryRecordData::class.java)
                listItemFavorite.add(recordCategoryData!!)
                adapterFavorite.notifyDataSetChanged()

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                val recordCategoryData = p0?.getValue(CategoryRecordData::class.java)
                listItemFavorite.remove(recordCategoryData)
                adapterFavorite.notifyDataSetChanged()
                onChangeItemCount?.invoke(0)

                if (listItemFavorite[0].listCategory.count() == 0) {
                    listItemFavorite.clear()
                }
            }
        }
        return onChildEventListener
    }

    companion object {
        private const val keyChild = "userCategoryProfile"
    }

}