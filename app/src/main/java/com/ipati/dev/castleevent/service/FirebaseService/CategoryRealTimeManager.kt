package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ListCategoryMenuAdapter

class CategoryRealTimeManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    private var CATEGORY_ALL: String = context.getString(R.string.categoryAll)
    private var CATEGORY_EDUCATION: String = context.getString(R.string.categoryEducation)
    private var CATEGORY_TECHNOLOGY: String = context.getString(R.string.categoryTechnology)
    private var CATEGORY_SPORT: String = context.getString(R.string.categorySport)

    private var contextManager: Context = context
    private var lifecycleManager: Lifecycle? = null
    private var listCategory: ArrayList<String> = ArrayList()


    var categoryAdapter: ListCategoryMenuAdapter = ListCategoryMenuAdapter(listCategory)

    init {
        lifecycleManager = lifecycle
        lifecycleManager?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        listCategory.apply {
            add(CATEGORY_ALL)
            add(CATEGORY_EDUCATION)
            add(CATEGORY_TECHNOLOGY)
            add(CATEGORY_SPORT)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        listCategory.clear()
    }
}