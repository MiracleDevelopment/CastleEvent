package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Glide.loadPhotoUserProfile
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username
import kotlinx.android.synthetic.main.activity_profile_user_fragment.*

class ProfileUserFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_profile_user_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialToolbar()
        initialEditText()
    }

    private fun initialToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_user_profile)
            supportActionBar?.apply {
                title = ""
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun initialEditText() {
        loadPhotoUserProfile(context, photoUrl.toString(), im_edit_photo_profile)

        ed_account_name_profile.setText(username)
        ed_email_profile.setText(userEmail)

        ed_account_name_profile.isEnabled = false
        ed_email_profile.isEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return true
    }

    companion object {
        fun newInstance(): ProfileUserFragment {
            return ProfileUserFragment().apply {
                arguments = Bundle()
            }
        }
    }
}

