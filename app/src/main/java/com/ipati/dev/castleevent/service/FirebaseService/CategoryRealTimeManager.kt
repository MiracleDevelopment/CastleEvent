package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*
import com.ipati.dev.castleevent.adapter.ListCategoryMenuAdapter
import com.ipati.dev.castleevent.model.Category.ALL
import com.ipati.dev.castleevent.model.Category.Education
import com.ipati.dev.castleevent.model.Category.Sport
import com.ipati.dev.castleevent.model.Category.Technology
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent

class CategoryRealTimeManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    private var CATEGORY_ALL: String = "ALL"
    private var CATEGORY_EDUCATION: String = "Education"
    private var CATEGORY_TECHNOLOGY: String = "Technology"
    private var CATEGORY_SPORT: String = "Sport"

    private var mContext: Context = context
    private var mLifecycle: Lifecycle? = null
    private var Ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mRef: DatabaseReference = Ref.child("eventItem").child("eventContinue")
    private var mListCategory: ArrayList<String> = ArrayList()
    private lateinit var addValueListener: ValueEventListener

    var mCategoryAdapter: ListCategoryMenuAdapter = ListCategoryMenuAdapter(mListCategory)

    init {
        mLifecycle = lifecycle
        mLifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        mListCategory.apply {
            add(CATEGORY_ALL)
            add(CATEGORY_EDUCATION)
            add(CATEGORY_TECHNOLOGY)
            add(CATEGORY_SPORT)
        }

        defaultCategoryItemCount()
        mRef.addValueEventListener(ValueEventListener())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mListCategory.clear()
        defaultCategoryItemCount()
        mRef.removeEventListener(addValueListener)

    }

    private fun ValueEventListener(): ValueEventListener {
        addValueListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(mContext, p0?.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot?) {
                for (itemsObject in p0?.children!!) {
                    val itemListEvent: ItemListEvent = itemsObject.getValue(ItemListEvent::class.java)!!
                    ALL += 1

                    when (itemListEvent.categoryName) {
                        CATEGORY_EDUCATION -> {
                            Education += 1
                        }
                        CATEGORY_TECHNOLOGY -> {
                            Technology += 1
                        }
                        CATEGORY_SPORT -> {
                            Sport += 1
                        }
                    }
                }

                mCategoryAdapter.notifyDataSetChanged()
            }
        }

        return addValueListener
    }

    private fun defaultCategoryItemCount() {
        ALL = 0
        Education = 0
        Technology = 0
        Sport = 0
    }
}