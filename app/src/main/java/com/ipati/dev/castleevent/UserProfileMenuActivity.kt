package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.fragment.UserProfileFragment

class UserProfileMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_menu)

        supportFragmentManager.beginTransaction().replace(R.id.frame_user_profile_menu_fragment
                , UserProfileFragment.newInstance(""))
                .commitNow()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }
}
