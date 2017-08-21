package com.ipati.dev.castleevent

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.model.OnBackPress
import com.ipati.dev.castleevent.model.ShowDetailListEvent

class ListEventActivity : AppCompatActivity(), ShowDetailListEvent, OnBackPress {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_list_event_layout
                        , ListEventFragment.newInstance("listEvent"))
                .commitNow()

    }

    override fun onShowDetailListEvent(eventId: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_list_event_layout
                        , ListDetailEventFragment.newInstance(eventId))
                .commitNow()
    }

    override fun onBackPressFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_list_event_layout
                        , ListEventFragment.newInstance("listEvent"))
                .commitNow()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
