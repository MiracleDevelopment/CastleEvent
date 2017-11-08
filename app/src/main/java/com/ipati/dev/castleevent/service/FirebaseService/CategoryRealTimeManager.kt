package com.ipati.dev.castleevent.service.FirebaseService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ListCategoryMenuAdapter

class CategoryRealTimeManager(context: Context, lifecycle: Lifecycle) : LifecycleObserver {
    private val contextManager: Context = context
    private val categoryALL: String = contextManager.getString(R.string.categoryAll)
    private val categoryEducation: String = contextManager.getString(R.string.categoryEducation)
    private val categoryTechnology: String = contextManager.getString(R.string.categoryTechnology)
    private val categorySport: String = contextManager.getString(R.string.categorySport)

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
            add(categoryALL)
            add(categoryEducation)
            add(categoryTechnology)
            add(categorySport)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        listCategory.clear()
    }
}