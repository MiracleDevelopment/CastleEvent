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
import java.util.*
import kotlin.collections.ArrayList

class ListEventFragment : BaseFragment() {
    private lateinit var realTimeDatabaseManager: RealTimeDatabaseManager
    private lateinit var categoryRealTimeDatabaseManager: CategoryRealTimeManager
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var itemCategoryChangeThai: List<ItemListEvent>

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
        bottomSheetBehavior = BottomSheetBehavior.from(view?.findViewById(R.id.bottom_sheet_category))
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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
            when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                else -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
        if (Locale.getDefault().language == "th") {
            when (categorySelect) {
                "การศึกษา" -> {
                    itemCategoryChangeThai = realTimeDatabaseManager.arrayItemList.
                            filter { it.categoryName == "Education" }
                }
                "เทคโนโลยี" -> {
                    itemCategoryChangeThai = realTimeDatabaseManager.arrayItemList.
                            filter { it.categoryName == "Technology" }
                }
                "กีฬา" -> {
                    itemCategoryChangeThai = realTimeDatabaseManager.arrayItemList.
                            filter { it.categoryName == "Sport" }
                }
                "ทั้งหมด" -> {
                    itemCategoryChangeThai = realTimeDatabaseManager.arrayItemList.
                            filter { it.categoryName == "ALL" }
                }
            }
            if (itemCategoryChangeThai.count() > 0) {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listItem = ArrayList(itemCategoryChangeThai))
                recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
            } else {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listItem = ArrayList(realTimeDatabaseManager.arrayItemList))
                recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
            }

            realTimeDatabaseManager.adapterListEvent?.onItemTransitionClickable = { view, width
                                                                                    , height
                                                                                    , transitionName
                                                                                    , eventId ->
                intentTransitionView(view, width, height, transitionName, eventId)
            }

        } else {
            val itemCategoryChangeUs: List<ItemListEvent> = realTimeDatabaseManager.arrayItemList.
                    filter { it.categoryName == categorySelect }


            if (itemCategoryChangeUs.count() > 0) {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listItem = ArrayList(itemCategoryChangeUs))
                recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
                realTimeDatabaseManager.adapterListEvent?.onItemTransitionClickable = { view, width
                                                                                        , height
                                                                                        , transitionName
                                                                                        , eventId ->
                    intentTransitionView(view, width, height, transitionName, eventId)
                }

            } else {
                realTimeDatabaseManager.adapterListEvent = ListEventAdapter(listItem = realTimeDatabaseManager.arrayItemList)
                recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
                realTimeDatabaseManager.adapterListEvent?.onItemTransitionClickable = { view, width
                                                                                        , height
                                                                                        , transitionName
                                                                                        , eventId ->
                    intentTransitionView(view, width, height, transitionName, eventId)
                }
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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        startActivity(intentAnimation, activityOptionsCompat.toBundle())

    }

    companion object {
        private const val listEventObject: String = "ListEventFragment"
        fun newInstance(nameObject: String): ListEventFragment {
            val listEventFragment = ListEventFragment()
            val bundle = Bundle()
            bundle.putString(listEventObject, nameObject)
            listEventFragment.arguments = bundle
            return listEventFragment
        }
    }
}
