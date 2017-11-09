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
                        , ProfileUserFragment.newInstance(), tagProfileUserFragment)
                .commitNow()
    }

    override fun onChangeProfile(msg: String, requestCode: Int) {
        val profileFragment = supportFragmentManager.findFragmentByTag(tagProfileUserFragment)
        profileFragment?.let {
            when (requestCode) {
                requestChangeUserName -> {
                    (profileFragment as ProfileUserFragment).apply {
                        onChangeUsername(msg)
                    }
                }

                requestChangePassword -> {
                    (profileFragment as ProfileUserFragment).apply {
                        onChangePassword(msg)
                    }
                }

                requestChangeEmail -> {
                    (profileFragment as ProfileUserFragment).apply {
                        onChangeEmail(msg)
                    }
                }

                requestChangePhoto -> {
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
        val loadingFragment: Fragment? = supportFragmentManager.findFragmentByTag(tagLoadingDialogFragment)
        loadingFragment?.let {
            (loadingFragment as LoadingDialogFragment).setProgressUploadTaskPhoto(progress)
        }
    }

    companion object {
        private const val tagProfileUserFragment = "ProfileUserFragment"
        private const val tagLoadingDialogFragment = "LoadingDialogFragment"
        private const val requestChangeUserName = 1008
        private const val requestChangePassword = 1009
        private const val requestChangeEmail = 1010
        private const val requestChangePhoto = 1111
    }

}
