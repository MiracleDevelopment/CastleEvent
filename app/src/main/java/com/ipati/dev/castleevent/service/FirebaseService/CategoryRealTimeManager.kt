package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.ipati.dev.castleevent.adapter.ListCategoryMenuAdapter

class CategoryRealTimeManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    private var CATEGORY_ALL: String = "ALL"
    private var CATEGORY_EDUCATION: String = "Education"
    private var CATEGORY_TECHNOLOGY: String = "Technology"
    private var CATEGORY_SPORT: String = "Sport"
    private var contextManager: Context = context
    private var mLifecycle: Lifecycle? = null
    private var mListCategory: ArrayList<String> = ArrayList()


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
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mListCategory.clear()


    }
}