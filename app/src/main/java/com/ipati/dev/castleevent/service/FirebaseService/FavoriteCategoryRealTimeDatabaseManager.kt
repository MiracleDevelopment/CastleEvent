package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.FavoriteMenuAdapter
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.service.RecordedEvent.CategoryRecordData


class FavoriteCategoryRealTimeDatabaseManager(lifecycle: Lifecycle) : LifecycleObserver {
    var listItemFavorite: ArrayList<CategoryRecordData> = ArrayList()
    var adapterFavorite: FavoriteMenuAdapter = FavoriteMenuAdapter(listItemFavorite)

    var onChangeItemCount: ((count: Int) -> Unit?)? = null
    var refDatabase: DatabaseReference? = null

    private var lifecycleManager: Lifecycle? = lifecycle
    private var ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var addValueEventListener: ValueEventListener? = setOnValueListener()
    private var addChildEventListener: ChildEventListener? = setChildEvent()

    init {
        lifecycleManager?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        ref.child(keyChild).addListenerForSingleValueEvent(addValueEventListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        ref.removeEventListener(addValueEventListener)

        refDatabase?.let {
            refDatabase?.removeEventListener(addChildEventListener)
        }
    }

    private fun setOnValueListener(): ValueEventListener? {
        return object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("onCancelled", p0.message.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                when {
                    p0.exists() -> {
                        if (p0.hasChild(uid)) {
                            refDatabase = ref.child(keyChild).child(uid)
                            refDatabase?.addChildEventListener(setChildEvent())
                        } else {
                            onChangeItemCount?.invoke(0)
                        }
                    }
                    else -> {
                        onChangeItemCount?.invoke(0)
                    }
                }
            }
        }
    }


    private fun setChildEvent(): ChildEventListener? {
        return object : ChildEventListener {
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
                onChangeItemCount?.invoke(1)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                val recordCategoryData = p0?.getValue(CategoryRecordData::class.java)
                listItemFavorite.remove(recordCategoryData)
                adapterFavorite.notifyDataSetChanged()
                onChangeItemCount?.invoke(0)

                when (listItemFavorite.count()) {
                    0 -> {
                        Log.d("listItemFavoriteSize", "Zero")
                    }
                    else -> {
                        when (listItemFavorite[0].listCategory.count()) {
                            0 -> {
                                listItemFavorite.clear()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val keyChild = "userCategoryProfile"
    }

}