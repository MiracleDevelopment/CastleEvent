package com.ipati.dev.castleevent

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ipati.dev.castleevent.fragment.ProfileUserFragment
import com.ipati.dev.castleevent.model.DismissDialogFragment

class ProfileUserActivity : AppCompatActivity(), DismissDialogFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_profile_user
                        , ProfileUserFragment.newInstance(), "ProfileUserFragment")
                .commitNow()
    }

    override fun onChangeProfile(msg: String, requestCode: Int) {
        when (requestCode) {
            1001 -> {
                val mProfileFragment = supportFragmentManager.findFragmentByTag("ProfileUserFragment")
                mProfileFragment?.let {
                    (mProfileFragment as ProfileUserFragment).apply {
                        onChangeUsername(msg)
                    }
                }
            }
            1002 -> {
                val mProfileFraagment = supportFragmentManager.findFragmentByTag("ProfileUserFragment")
                mProfileFraagment?.let {
                    (mProfileFraagment as ProfileUserFragment).apply {
                        onChangePassword(msg)
                    }
                }
            }
            1003 -> {
                val mProfileFragment = supportFragmentManager.findFragmentByTag("ProfileUserFragment")
                mProfileFragment?.let {
                    (mProfileFragment as ProfileUserFragment).onChangeEmail(msg)
                }
            }
            1004 -> {
                val mProfileFragment = supportFragmentManager.findFragmentByTag("ProfileUserFragment")
                mProfileFragment?.let {
                    (mProfileFragment as ProfileUserFragment).apply {
                        onChangeUserPhoto(msg)
                    }
                }
            }
        }
    }

}
