package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.fragment.ListEventFragment

class ListEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)

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
