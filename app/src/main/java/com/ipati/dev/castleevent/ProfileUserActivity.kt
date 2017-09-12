package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.fragment.ProfileUserFragment

class ProfileUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_profile_user
                        , ProfileUserFragment.newInstance())
                .commitNow()
    }
}
