package com.ipati.dev.castleevent

import android.app.Dialog
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.widget.Toast
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.extension.onShowDialog
import com.ipati.dev.castleevent.fragment.ProfileUserFragment
import com.ipati.dev.castleevent.fragment.loading.LoadingDialogFragment
import com.ipati.dev.castleevent.model.DismissDialogFragment
import com.ipati.dev.castleevent.model.OnProgressPhotoUser

class ProfileUserActivity : BaseAppCompatActivity(), DismissDialogFragment, OnProgressPhotoUser {


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
        val profileFragment = supportFragmentManager.findFragmentByTag("ProfileUserFragment")
        profileFragment?.let {
            when (requestCode) {
                1008 -> {
                    (profileFragment as ProfileUserFragment).apply {
                        onChangeUsername(msg)
                    }
                }

                1009 -> {
                    (profileFragment as ProfileUserFragment).apply {
                        onChangePassword(msg)
                    }
                }

                1010 -> {
                    (profileFragment as ProfileUserFragment).apply {
                        onChangeEmail(msg)
                    }
                }

                1111 -> {
                    (profileFragment as ProfileUserFragment).apply {
                        onChangeUserPhoto(msg)
                    }

                }
                else -> {

                }
            }
        }

    }

    override fun setProgressUserPhoto(progress: Int) {
        val loadingFragment: Fragment? = supportFragmentManager.findFragmentByTag("LoadingDialogFragment")
        loadingFragment?.let {
            (loadingFragment as LoadingDialogFragment).setProgressUploadTaskPhoto(progress)
        }
    }

}
