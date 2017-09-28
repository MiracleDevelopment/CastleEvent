package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.LinearLayout
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.LoadingCategory
import com.ipati.dev.castleevent.model.LoadingDetailEvent
import com.ipati.dev.castleevent.model.OnCancelAnimationTouch
import com.ipati.dev.castleevent.service.FirebaseService.CategoryRealTimeManager
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseManager
import com.ipati.dev.castleevent.utill.animation.AnimationManager
import kotlinx.android.synthetic.main.activity_list_event_fragment.*
import kotlinx.android.synthetic.main.custom_bottom_sheet_category.*

class ListEventFragment : BaseFragment() {
    private lateinit var realTimeDatabaseManager: RealTimeDatabaseManager
    private lateinit var mCategoryRealTimeDatabaseManager: CategoryRealTimeManager
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var mAnimationManager: AnimationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeDatabaseManager = RealTimeDatabaseManager(context, lifecycle)
        mCategoryRealTimeDatabaseManager = CategoryRealTimeManager(context, lifecycle)
        mAnimationManager = AnimationManager(context)
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
        realTimeDatabaseManager.adapterListEvent?.setOnClickItemEvent(object : LoadingDetailEvent {
            override fun setOnLoadingDetailEvent(target: View?, eventId: Long) {

            }
        })

        realTimeDatabaseManager.adapterListEvent?.setOnCancelTouchItemEvent(object : OnCancelAnimationTouch {
            override fun setOnCancelTouch(target: View?, eventId: Long) {
                mAnimationManager.setEventId(eventId = eventId)
                mAnimationManager.onLoadingTranslateZ().addUpdateListener { valueAnimator ->
                    target?.translationZ = valueAnimator.animatedValue as Float
                }

                mAnimationManager.onLoadingTranslateY().addUpdateListener { valueAnimator ->
                    target?.translationY = valueAnimator.animatedValue as Float
                }


            }
        })
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
        recycler_bottom_sheet.adapter = mCategoryRealTimeDatabaseManager.mCategoryAdapter

        mCategoryRealTimeDatabaseManager.mCategoryAdapter.setOnChangeCategory(object : LoadingCategory {
            override fun setOnChangeCategory(selectCategory: String) {
                realTimeDatabaseManager.mCategory = selectCategory
                onRefreshItemList()
            }

        })


    }

    private fun onRefreshItemList() {
        realTimeDatabaseManager.onChangeCategory()
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
