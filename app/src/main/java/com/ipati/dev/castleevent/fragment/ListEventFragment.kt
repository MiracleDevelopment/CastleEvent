package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.LinearLayout
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ListEventAdapter
import com.ipati.dev.castleevent.model.LoadingCategory
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import com.ipati.dev.castleevent.service.FirebaseService.CategoryRealTimeManager
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_list_event_fragment.*
import kotlinx.android.synthetic.main.custom_bottom_sheet_category.*
import kotlinx.android.synthetic.main.custom_list_event_adapter_layout.view.*
import kotlin.collections.ArrayList

class ListEventFragment : BaseFragment() {
    private lateinit var realTimeDatabaseManager: RealTimeDatabaseManager
    private lateinit var categoryRealTimeDatabaseManager: CategoryRealTimeManager
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeDatabaseManager = RealTimeDatabaseManager(context, lifecycle)
        categoryRealTimeDatabaseManager = CategoryRealTimeManager(context, lifecycle)
        activity.invalidateOptionsMenu()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_list_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialRecyclerView()
        initialBottomSheet()
        initialBottomSheetCategory()

    }

    private fun initialRecyclerView() {
        recycler_list_event.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_list_event.itemAnimator = DefaultItemAnimator()
        recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent

        realTimeDatabaseManager.adapterListEvent?.onItemTransitionClickable = { view, width, height, transitionName, eventId ->
            intentTransitionView(view, width, height, transitionName, eventId)
        }
    }

    private fun initialBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(view?.findViewById(R.id.bottom_sheet_category))
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                }
            }
        })

        im_header_bottom_sheet_category.setOnClickListener {
            when (mBottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                else -> {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

    }

    private fun initialBottomSheetCategory() {
        recycler_bottom_sheet.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recycler_bottom_sheet.itemAnimator = DefaultItemAnimator()
        recycler_bottom_sheet.adapter = categoryRealTimeDatabaseManager.categoryAdapter

        categoryRealTimeDatabaseManager.categoryAdapter.setOnChangeCategory(object : LoadingCategory {
            override fun setOnChangeCategory(selectCategory: String) {
                onRefreshItemList(selectCategory)
            }
        })
    }

    private fun onRefreshItemList(categorySelect: String) {
        val itemCategoryChange: List<ItemListEvent> = realTimeDatabaseManager.arrayItemList.
                filter { it.categoryName == categorySelect }

        if (itemCategoryChange.count() > 0) {
            realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listItem = ArrayList(itemCategoryChange))
            recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
            realTimeDatabaseManager.adapterListEvent?.onItemTransitionClickable = { view, width, height, transitionName, eventId ->
                intentTransitionView(view, width, height, transitionName, eventId)
            }

        } else {
            realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listItem = realTimeDatabaseManager.arrayItemList)
            recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
            realTimeDatabaseManager.adapterListEvent?.onItemTransitionClickable = { view, width, height, transitionName, eventId ->
                intentTransitionView(view, width, height, transitionName, eventId)
            }
        }
    }

    private fun intentTransitionView(view: View?, width: Int, height: Int, transitionName: String, eventId: Long) {
        val intentAnimation = Intent(context, ListDetailEventActivity::class.java)

        val activityOptionsCompat: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity
                        , view?.custom_im_cover_list_event
                        , ViewCompat.getTransitionName(view))

        intentAnimation.putExtra("width", width)
        intentAnimation.putExtra("height", height)
        intentAnimation.putExtra("transitionName", transitionName)
        intentAnimation.putExtra("eventId", eventId)
        startActivity(intentAnimation, activityOptionsCompat.toBundle())
    }

    //Todo: Calling From Activity
    fun onShowBottomSheetCategory() {
        when (mBottomSheetBehavior.state) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            BottomSheetBehavior.STATE_COLLAPSED -> {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    //Todo: Calling From Activity
    fun onDisableBottomSheetCategory() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }


    companion object {
        private var listEventObject: String = "ListEventFragment"
        fun newInstance(nameObject: String): ListEventFragment {
            val listEventFragment = ListEventFragment()
            val bundle = Bundle()
            bundle.putString(listEventObject, nameObject)
            listEventFragment.arguments = bundle
            return listEventFragment
        }
    }
}
