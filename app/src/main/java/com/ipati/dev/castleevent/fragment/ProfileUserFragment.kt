package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.fresco.loadPhotoUserProfile
import com.ipati.dev.castleevent.model.UserProfileUpdate
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username
import kotlinx.android.synthetic.main.activity_profile_user_fragment.*

class ProfileUserFragment : Fragment(), View.OnClickListener {
    private var mRequestUsername: Int = 1001
    private var mRequestPassword: Int = 1002
    private var mRequestEmail: Int = 1003
    private var REQUEST_PHOTO: Int = 1111

    private lateinit var mUserProfileChange: UserProfileUpdate
    private lateinit var mChangeCustomProfile: ChangeCustomProfileDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mUserProfileChange = UserProfileUpdate(context, activity)
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

        edit_username.setOnClickListener { view -> onClick(view) }
        edit_pass.setOnClickListener { view -> onClick(view) }
        edit_email_profile.setOnClickListener { view -> onClick(view) }

        tv_record_profile.setOnClickListener { view -> onClick(view) }

        im_edit_photo_profile.setOnClickListener { view ->
            onClick(view)

        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.edit_username -> {
                mChangeCustomProfile = ChangeCustomProfileDialogFragment.newInstance("Username", username.toString(), mRequestUsername)
                mChangeCustomProfile.isCancelable = false
                mChangeCustomProfile.show(activity.supportFragmentManager, "ChangeProfile")
            }

            R.id.edit_pass -> {
                mChangeCustomProfile = ChangeCustomProfileDialogFragment.newInstance("Password", "", mRequestPassword)
                mChangeCustomProfile.isCancelable = false
                mChangeCustomProfile.show(activity.supportFragmentManager, "ChangeProfile")
            }


            R.id.edit_email_profile -> {
                mChangeCustomProfile = ChangeCustomProfileDialogFragment.newInstance("userEmail", userEmail.toString(), mRequestEmail)
                mChangeCustomProfile.isCancelable = false
                mChangeCustomProfile.show(activity.supportFragmentManager, "ChangeProfile")
            }

            R.id.tv_record_profile -> {
                activity.supportFinishAfterTransition()
            }

            R.id.im_edit_photo_profile -> {
                val intentPhoto = Intent(Intent.ACTION_GET_CONTENT)
                intentPhoto.type = "image/*"
                startActivityForResult(Intent.createChooser(intentPhoto, "Choose Image Profile"), REQUEST_PHOTO)
            }
        }
    }


    //Todo: Calling From Dialog
    fun onChangeUsername(mUsername: String) {
        ed_account_name_profile.setText(mUsername)
    }

    //Todo: Calling From Dialog
    fun onChangePassword(mPassword: String) {
        ed_account_pass_profile.setText(mPassword)
        ed_re_account_password_profile.setText(mPassword)
    }

    //Todo: Calling From Dialog
    fun onChangeEmail(mUserEmail: String) {
        ed_email_profile.setText(mUserEmail)
    }

    //Todo: Calling From Dialog
    fun onChangeUserPhoto(mUserUri: String) {
        loadPhotoUserProfile(context, mUserUri, im_edit_photo_profile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_PHOTO ->
                data?.let {
                    mUserProfileChange.onUpdatePhotoUser(data.data)
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
        }
        return true
    }

    companion object {
        fun newInstance(): ProfileUserFragment = ProfileUserFragment().apply {
            arguments = Bundle()
        }
    }
}

