package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ExpireListEventAdapter
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import com.ipati.dev.castleevent.service.FirebaseService.ExpireRealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_expire_event_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class ExpireEventFragment : Fragment() {
    private lateinit var expireRealTimeDatabase: ExpireRealTimeDatabaseManager
    var changeCategory: ((msg: String) -> Unit) = { msg: String ->
        when (Locale.getDefault().language) {
            "en" -> {
                setChangeCategoryEN(msg)
            }
            else -> {
                setChangeCategoryTH(msg)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_expire_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expireRealTimeDatabase = ExpireRealTimeDatabaseManager(lifecycle)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recycler_expire_event.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_expire_event.itemAnimator = DefaultItemAnimator()
        setAdapterRecyclerView()
    }

    //Todo: EditText Filter
    fun setOnSearchListener(eventName: String) {
        val listItemEvent = expireRealTimeDatabase.listItemEventExpire.filter { it.eventName.contains(eventName, true) }
        when (listItemEvent.count()) {
            0 -> {
                expireRealTimeDatabase.adapterExpire = ExpireListEventAdapter(expireRealTimeDatabase.listItemEventExpire)
                setAdapterRecyclerView()
            }
            else -> {
                expireRealTimeDatabase.adapterExpire = ExpireListEventAdapter(listChangeItem(listItemEvent))
                setAdapterRecyclerView()
            }
        }

    }

    //Todo categoryEnglish
    private fun setChangeCategoryEN(category: String) {
        expireRealTimeDatabase.adapterExpire = ExpireListEventAdapter(listItemEvent(category))
        setAdapterRecyclerView()
    }

    //Todo: categoryThai
    private fun setChangeCategoryTH(category: String) {
        when (category) {
            context.getString(R.string.categoryAll) -> {
                expireRealTimeDatabase.adapterExpire = ExpireListEventAdapter(expireRealTimeDatabase.listItemEventExpire)
                setAdapterRecyclerView()
            }

            context.getString(R.string.categoryEducation) -> {
                val listItemCategory = expireRealTimeDatabase.listItemEventExpire.filter { it.eventCategory == "Education" }
                expireRealTimeDatabase.adapterExpire = ExpireListEventAdapter(listChangeItem(listItemCategory))
                setAdapterRecyclerView()
            }

            context.getString(R.string.categoryTechnology) -> {
                val listItemCategory = expireRealTimeDatabase.listItemEventExpire.filter { it.eventCategory == "Technology" }
                expireRealTimeDatabase.adapterExpire = ExpireListEventAdapter(listChangeItem(listItemCategory))
                setAdapterRecyclerView()
            }

            else -> {
                val listItemCategory = expireRealTimeDatabase.listItemEventExpire.filter { it.eventCategory == "Sport" }
                expireRealTimeDatabase.adapterExpire = ExpireListEventAdapter(listChangeItem(listItemCategory))
                setAdapterRecyclerView()
            }
        }
    }

    //Todo : ConvertListToArrayList
    private fun listChangeItem(list: List<ItemListEvent>): ArrayList<ItemListEvent> {
        return ArrayList(list)
    }

    private fun listItemEvent(category: String): ArrayList<ItemListEvent> {
        return when (category) {
            context.getString(R.string.categoryAll) -> {
                ArrayList(expireRealTimeDatabase.listItemEventExpire)
            }

            context.getString(R.string.categoryEducation) -> {
                val listEventItem = expireRealTimeDatabase.listItemEventExpire.filter { it.eventCategory == category }
                ArrayList(listEventItem)
            }

            context.getString(R.string.categoryTechnology) -> {
                val listEventItem = expireRealTimeDatabase.listItemEventExpire.filter { it.eventCategory == category }
                ArrayList(listEventItem)
            }
            else -> {
                val listEventItem = expireRealTimeDatabase.listItemEventExpire.filter { it.eventCategory == category }
                ArrayList(listEventItem)
            }

        }
    }

    private fun setAdapterRecyclerView() {
        recycler_expire_event.adapter = expireRealTimeDatabase.adapterExpire
        expireRealTimeDatabase.adapterExpire.callBackExpireListEventAdapter = { eventId, eventCategory, width, height, transitionName, viewTransition, status ->
            val intentExpire = Intent(context, ListDetailEventActivity::class.java)
            intentExpire.putExtra("eventId", eventId)
            intentExpire.putExtra("width", width)
            intentExpire.putExtra("height", height)
            intentExpire.putExtra("transitionName", transitionName)
            intentExpire.putExtra("eventCategory", eventCategory)
            intentExpire.putExtra("status", status)

            val activityOptionCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity
                    , viewTransition
                    , ViewCompat.getTransitionName(viewTransition))

            startActivity(intentExpire, activityOptionCompat.toBundle())
        }
    }

    companion object {
        fun newInstance(): ExpireEventFragment = ExpireEventFragment().apply { arguments = Bundle() }
    }
}
