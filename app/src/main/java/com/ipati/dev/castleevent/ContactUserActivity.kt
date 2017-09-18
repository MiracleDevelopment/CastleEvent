package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.fragment.ContactUserFragment

class ContactUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_user)

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_contact
                        , ContactUserFragment.newInstance())
                .commitNow()
    }
}
