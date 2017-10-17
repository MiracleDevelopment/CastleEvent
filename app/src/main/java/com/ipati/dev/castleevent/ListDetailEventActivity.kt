package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.SharedElementCallback
import android.view.View
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import com.ipati.dev.castleevent.model.LoadingDialogListener


class ListDetailEventActivity : BaseAppCompatActivity(), LoadingDialogListener {
    private var bundle: Bundle? = null
    private var eventId: Long? = null
    private var widthView: Int? = null
    private var heightView: Int? = null
    private var transitionName: String? = null
    private var TAG_LIST_FRAGMENT: String = "ListDetailFragment"
    private var fragment: Fragment? = null

    private lateinit var mListDetailFragment: ListDetailEventFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail_event)
        bundle = intent.extras
        bundle?.let {
            eventId = bundle?.getLong("eventId")
            widthView = bundle?.getInt("width")
            heightView = bundle?.getInt("height")
            transitionName = bundle?.getString("transitionName")
            mListDetailFragment = ListDetailEventFragment.newInstance(widthView!!, heightView!!, transitionName!!, eventId!!)

            supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_list_detail_event
                            , mListDetailFragment
                            , TAG_LIST_FRAGMENT)
                    .commitNow()
        }


    }

    override fun onPositiveClickable(statusLoading: Boolean) {
        fragment = supportFragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT)
        fragment?.let {
            (fragment as ListDetailEventFragment).onPositiveConfirmFragment()
        }

    }

    override fun onNegativeClickable(statusLoading: Boolean) {
        fragment = supportFragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT)
        fragment?.let {
            (fragment as ListDetailEventFragment).onNegativeConfirmFragment()
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementEnd(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, sharedElementSnapshots: MutableList<View>?) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                sharedElements?.let {
                    sharedElements.clear()
                    sharedElementNames?.clear()
                    sharedElementSnapshots?.clear()
                }
                supportFinishAfterTransition()
            }
        })
    }
}
