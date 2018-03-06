package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ComingListEventAdapter
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import com.ipati.dev.castleevent.service.FirebaseService.ComingRealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_comming_event_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class ComingEventFragment : BaseFragment() {
    private lateinit var comingRealTimeDatabaseManager: ComingRealTimeDatabaseManager
    var changeCategory: ((msg: String) -> Unit) = { msg: String ->
        when (Locale.getDefault().language) {
            "en" -> {
                setChangeCategoryEn(msg)
            }

            else -> {
                setChangeCategoryTH(msg)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_comming_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        comingRealTimeDatabaseManager = ComingRealTimeDatabaseManager(lifecycle)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recycler_coming_event.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_coming_event.itemAnimator = DefaultItemAnimator()
        setAdapterRecyclerView()
    }

    //Todo: EditText Filter
    fun setOnSearchListener(eventName: String) {
        val listItemEvent = comingRealTimeDatabaseManager.listItemEventComing.filter { it.eventName.contains(eventName, true) }
        when (listItemEvent.count()) {
            0 -> {
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(comingRealTimeDatabaseManager.listItemEventComing)
                setAdapterRecyclerView()
            }
            else -> {
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemEvent))
                setAdapterRecyclerView()
            }
        }
    }

    //Todo: categoryEnglish
    private fun setChangeCategoryEn(category: String) {
        comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listItemCategory(category))
        setAdapterRecyclerView()
    }

    //Todo: categoryThai
    private fun setChangeCategoryTH(category: String) {
        when (category) {
            context.getString(R.string.categoryAll) -> {
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(comingRealTimeDatabaseManager.listItemEventComing)
                setAdapterRecyclerView()
            }

            context.getString(R.string.categoryEducation) -> {
                val listItemCategory = comingRealTimeDatabaseManager.listItemEventComing.filter { it.eventCategory == "Education" }
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemCategory))
                setAdapterRecyclerView()
            }

            context.getString(R.string.categoryTechnology) -> {
                val listItemCategory = comingRealTimeDatabaseManager.listItemEventComing.filter { it.eventCategory == "Technology" }
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemCategory))
                setAdapterRecyclerView()
            }

            else -> {
                val listItemCategory = comingRealTimeDatabaseManager.listItemEventComing.filter { it.eventCategory == "Sport" }
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemCategory))
                setAdapterRecyclerView()
            }
        }
    }

    private fun setAdapterRecyclerView() {
        recycler_coming_event.adapter = comingRealTimeDatabaseManager.adapterListComing
        comingRealTimeDatabaseManager.adapterListComing.callBackComingListEventAdapter = { eventId, eventCategory, width
                                                                                           , height
                                                                                           , transitionName
                                                                                           , viewTransition
                                                                                           , status ->
            val intentListDetail = Intent(context, ListDetailEventActivity::class.java)
            intentListDetail.putExtra("eventId", eventId)
            intentListDetail.putExtra("width", width)
            intentListDetail.putExtra("height", height)
            intentListDetail.putExtra("transitionName", transitionName)
            intentListDetail.putExtra("eventCategory", eventCategory)
            intentListDetail.putExtra("status", status)

            val activityOptionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity
                    , viewTransition
                    , ViewCompat.getTransitionName(viewTransition))

            startActivity(intentListDetail, activityOptionCompat.toBundle())
        }
    }

    private fun listItemCategory(category: String): ArrayList<ItemListEvent> {
        return when (category) {
            context.getString(R.string.categoryAll) -> {
                ArrayList(comingRealTimeDatabaseManager.listItemEventComing)
            }

            context.getString(R.string.categoryEducation) -> {
                val listItemEvent = comingRealTimeDatabaseManager.listItemEventComing.filter { it.eventCategory == category }
                ArrayList(listItemEvent)
            }

            context.getString(R.string.categoryTechnology) -> {
                val listItemEvent = comingRealTimeDatabaseManager.listItemEventComing.filter { it.eventCategory == category }
                ArrayList(listItemEvent)
            }

            else -> {
                val listItemEvent = comingRealTimeDatabaseManager.listItemEventComing.filter { it.eventCategory == category }
                ArrayList(listItemEvent)
            }
        }
    }

    //Todo : ConvertListToArrayList
    private fun listChangeCategory(listEvent: List<ItemListEvent>): ArrayList<ItemListEvent> {
        return ArrayList(listEvent)
    }

    companion object {
        fun newInstance(): ComingEventFragment = ComingEventFragment().apply { arguments = Bundle() }
    }
}
