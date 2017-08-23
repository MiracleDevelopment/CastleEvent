package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.fragment.ListDetailEventFragment

class ListDetailEventActivity : AppCompatActivity() {
    private var bundle: Bundle? = null
    private var eventId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail_event)
        bundle = intent.extras
        bundle?.let {

            eventId = bundle?.getLong("eventId")
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_list_detail_event
                            , ListDetailEventFragment.newInstance(eventId!!)).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
