package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.DraweeTransition
import com.facebook.drawee.view.SimpleDraweeView
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ListEventAdapter
import com.ipati.dev.castleevent.extension.onShowToast
import com.ipati.dev.castleevent.model.GoogleCalendar.categoryNameEvent
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import com.ipati.dev.castleevent.service.FirebaseService.CategoryRealTimeManager
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseManager
import icepick.State
import kotlinx.android.synthetic.main.activity_list_event_fragment.*
import kotlinx.android.synthetic.main.custom_list_event_adapter_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class ListEventFragment : BaseFragment() {
    private lateinit var realTimeDatabaseManager: RealTimeDatabaseManager
    private lateinit var categoryRealTimeDatabaseManager: CategoryRealTimeManager
    val changeCategory: ((msg: String) -> Unit) = { msg: String ->
        when (Locale.getDefault().language) {
            "en" -> {
                setChangeCategoryEN(msg)
            }

            else -> {
                setChangeCategoryTH(msg)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeDatabaseManager = RealTimeDatabaseManager(context, lifecycle)
        categoryRealTimeDatabaseManager = CategoryRealTimeManager(context, lifecycle)
        activity.invalidateOptionsMenu()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_list_event_fragment, container, false)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialRecyclerView()
    }

    private fun initialRecyclerView() {
        recycler_list_event.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_list_event.itemAnimator = DefaultItemAnimator()
        setShareElementTransition()
    }

    //Todo: EditText Filter
    fun setOnSearchListener(eventName: String) {
        val listItemCategory = realTimeDatabaseManager.arrayItemList.filter { it.eventName.contains(eventName, true) }

        when (listItemCategory.count()) {
            0 -> {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(realTimeDatabaseManager.arrayItemList)
                setShareElementTransition()
            }
            else -> {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listChangeItem(listItemCategory))
                setShareElementTransition()
            }
        }
    }

    //Todo: categoryEnglish
    private fun setChangeCategoryEN(category: String) {
        val listItemCategory = realTimeDatabaseManager.arrayItemList.filter { it.categoryName == category }
        when (listItemCategory.count()) {
            0 -> {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(realTimeDatabaseManager.arrayItemList)
                setShareElementTransition()
            }

            else -> {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listChangeItem(listItemCategory))
                setShareElementTransition()
            }
        }
    }

    //Todo: categoryThai
    private fun setChangeCategoryTH(category: String) {
        when (category) {
            context.getString(R.string.categoryAll) -> {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(realTimeDatabaseManager.arrayItemList)
                setShareElementTransition()
            }

            context.getString(R.string.categoryEducation) -> {
                val listItemCategory = realTimeDatabaseManager.arrayItemList.filter { it.categoryName == "Education" }
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listChangeItem(listItemCategory))
                setShareElementTransition()
            }

            context.getString(R.string.categoryTechnology) -> {
                val listItemCategory = realTimeDatabaseManager.arrayItemList.filter { it.categoryName == "Technology" }
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listChangeItem(listItemCategory))
                setShareElementTransition()
            }

            context.getString(R.string.categorySport) -> {
                val listItemCategory = realTimeDatabaseManager.arrayItemList.filter { it.categoryName == "Sport" }
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listChangeItem(listItemCategory))
                setShareElementTransition()
            }
        }

    }

    //Todo: ConvertListToArrayList
    private fun listChangeItem(list: List<ItemListEvent>): ArrayList<ItemListEvent> = ArrayList(list)


    private fun setShareElementTransition() {
        recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
        realTimeDatabaseManager.adapterListEvent?.onItemTransitionClickable = { view, width, height, transitionName
                                                                                , eventId
                                                                                , status ->
            intentTransitionView(view, width, height, transitionName, eventId, status)
        }
    }


    private fun intentTransitionView(view: View?, width: Int, height: Int, transitionName: String, eventId: Long, status: Int) {
        val intentAnimation = Intent(context, ListDetailEventActivity::class.java)

        val activityOptionsCompat: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity
                        , view?.custom_im_cover_list_event
                        , ViewCompat.getTransitionName(view))

        intentAnimation.putExtra("width", width)
        intentAnimation.putExtra("height", height)
        intentAnimation.putExtra("transitionName", transitionName)
        intentAnimation.putExtra("eventId", eventId)
        intentAnimation.putExtra("status", status)
        startActivity(intentAnimation, activityOptionsCompat.toBundle())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val listEventObject: String = "ListEventFragment"
        private const val categoryObject: String = "categoryName"
        fun newInstance(nameObject: String): ListEventFragment {
            val listEventFragment = ListEventFragment()
            val bundle = Bundle()
            bundle.putString(listEventObject, nameObject)
            listEventFragment.arguments = bundle
            return listEventFragment
        }
    }
}
