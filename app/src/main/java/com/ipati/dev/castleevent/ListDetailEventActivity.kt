package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import com.ipati.dev.castleevent.model.LoadingDialogListener

class ListDetailEventActivity : AppCompatActivity(), LoadingDialogListener {
    private var bundle: Bundle? = null
    private var eventId: Long? = null
    private var TAG_LIST_FRAGMENT: String = "ListDetailFragment"
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail_event)
        bundle = intent.extras
        bundle?.let {

            eventId = bundle?.getLong("eventId")
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_list_detail_event
                            , ListDetailEventFragment.newInstance(eventId!!), TAG_LIST_FRAGMENT).commit()
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
        finish()
    }
}
