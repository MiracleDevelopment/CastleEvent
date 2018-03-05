package com.ipati.dev.castleevent

import android.content.Intent
import android.os.Bundle
import android.support.transition.Transition
import android.support.transition.TransitionInflater
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.extension.onShowMissingDialog
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import com.ipati.dev.castleevent.model.LoadingDialogListener
import com.ipati.dev.castleevent.model.OnMissingConfirm


class ListDetailEventActivity : BaseAppCompatActivity(), LoadingDialogListener, OnMissingConfirm {
    private lateinit var listDetailFragment: ListDetailEventFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail_event)
        setStateNetWorking { state: Boolean ->
            if (state) {
                setUpFragment()
            } else {
                onShowMissingDialog(this, "Internet isn't Work", 1010).apply {
                    callBackNetWork = {
                        startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
                        supportFinishAfterTransition()
                    }
                }
            }
        }
    }

    private fun setUpFragment() {
        intent.extras?.let {
            val eventId = it.getLong("eventId")
            val eventCategory = it.getString("eventCategory")
            val widthView = it.getInt("width")
            val heightView = it.getInt("height")
            val transitionName = it.getString("transitionName")
            val statusType = it.getInt("status")
            listDetailFragment = ListDetailEventFragment.newInstance(widthView, heightView, transitionName, eventId, eventCategory, statusType)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_list_detail_event
                            , listDetailFragment
                            , TAG_LIST_FRAGMENT)
                    .commitNow()
        }
    }

    override fun onPositiveClickable(statusLoading: Boolean) {
        supportFragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT)?.let {
            (it as ListDetailEventFragment).onPositiveConfirmFragment()
        }
    }

    override fun onNegativeClickable(statusLoading: Boolean) {
        supportFragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT)?.let {
            (it as ListDetailEventFragment).onNegativeConfirmFragment()
        }
    }

    override fun onMissingDialogConfirm() {
        supportFragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT)?.let {
            (it as ListDetailEventFragment).onMissingDialogConfirmFragment()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }

    companion object {
        private const val TAG_LIST_FRAGMENT: String = "ListDetailFragment"
    }
}
