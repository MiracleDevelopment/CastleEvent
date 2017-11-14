package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ComingListEventAdapter
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import com.ipati.dev.castleevent.service.FirebaseService.ComingRealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_comming_event_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class ComingEventFragment : Fragment() {
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
        comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemEvent))
        setAdapterRecyclerView()
    }

    //Todo: categoryEnglish
    private fun setChangeCategoryEn(category: String) {
        val listItemList = comingRealTimeDatabaseManager.listItemEventComing.filter { it.categoryName == category }
        when (listItemList.count()) {
            0 -> {
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(comingRealTimeDatabaseManager.listItemEventComing)
                setAdapterRecyclerView()
            }
            else -> {
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(ArrayList(listItemList))
                setAdapterRecyclerView()
            }
        }
    }

    //Todo: categoryThai
    private fun setChangeCategoryTH(category: String) {
        when (category) {
            context.getString(R.string.categoryAll) -> {
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(comingRealTimeDatabaseManager.listItemEventComing)
                setAdapterRecyclerView()
            }

            context.getString(R.string.categoryEducation) -> {
                val listItemCategory = comingRealTimeDatabaseManager.listItemEventComing.filter { it.categoryName == "Education" }
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemCategory))
                setAdapterRecyclerView()
            }
            context.getString(R.string.categoryTechnology) -> {
                val listItemCategory = comingRealTimeDatabaseManager.listItemEventComing.filter { it.categoryName == "Technology" }
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemCategory))
                setAdapterRecyclerView()
            }
            else -> {
                val listItemCategory = comingRealTimeDatabaseManager.listItemEventComing.filter { it.categoryName == "Sport" }
                comingRealTimeDatabaseManager.adapterListComing = ComingListEventAdapter(listChangeCategory(listItemCategory))
                setAdapterRecyclerView()
            }
        }
    }

    private fun setAdapterRecyclerView() {
        recycler_coming_event.adapter = comingRealTimeDatabaseManager.adapterListComing
    }

    //Todo : ConvertListToArrayList
    private fun listChangeCategory(listEvent: List<ItemListEvent>): ArrayList<ItemListEvent> {
        return ArrayList(listEvent)
    }

    companion object {
        fun newInstance(): ComingEventFragment = ComingEventFragment().apply { arguments = Bundle() }
    }
}
