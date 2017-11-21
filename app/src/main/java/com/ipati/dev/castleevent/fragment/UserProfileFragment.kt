package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ipati.dev.castleevent.*
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.fragment.loading.LogOutFragmentDialog
import com.ipati.dev.castleevent.model.Fresco.loadPhotoProfileUser
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_user_profile_fragment.*

class UserProfileFragment : BaseFragment() {
    private lateinit var sharePreferenceSettingMenuList: SharePreferenceSettingManager
    private lateinit var logOutDialogFragment: LogOutFragmentDialog
    private lateinit var authUser: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_user_profile_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        authUser = FirebaseAuth.getInstance()
        sharePreferenceSettingMenuList = SharePreferenceSettingManager(context)

        authListener = FirebaseAuth.AuthStateListener { fireBaseAuth: FirebaseAuth ->
            val fireBaseUser: FirebaseUser? = fireBaseAuth.currentUser
            fireBaseUser?.let {
                username = fireBaseUser.displayName
                userEmail = fireBaseUser.email
                photoUrl = fireBaseUser.photoUrl.toString()

                loadPhotoProfileUser(context, photoUrl, im_user_profile_photo)

                tv_user_profile_name_full.text = username
                tv_user_profile_email.text = userEmail
            } ?: activity.finish()
        }
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_user_menu_profile_fragment)
            supportActionBar?.apply {
                title = ""
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    private fun onShowDialogLogOut() {
        logOutDialogFragment = LogOutFragmentDialog.newInstance("กรุณายืนยัน", "คุณต้องการออกจากระบบ ใช่ / ไม่")
        logOutDialogFragment.isCancelable = false
        logOutDialogFragment.show(activity.supportFragmentManager, "LogOutDialogFragment")

        logOutDialogFragment.onClickPositiveLogOut = {
            FirebaseAuth.getInstance().signOut()
            activity.supportFinishAfterTransition()
        }
    }

    private fun initialUserProfile() {
        tv_edit_profile.setOnClickListener {
            val intentProfile = Intent(context, ProfileUserActivity::class.java)
            startActivity(intentProfile)
        }

        card_my_order.setOnClickListener {
            val intentMyOrder = Intent(context, MyOrderActivity::class.java)
            startActivity(intentMyOrder)
        }

        card_calendar.setOnClickListener {
            val intentCalendar = Intent(context, CalendarActivity::class.java)
            startActivity(intentCalendar)
        }

        card_contact.setOnClickListener {
            val intentContact = Intent(context, ContactUserActivity::class.java)
            startActivity(intentContact)
        }

        card_my_favorite.setOnClickListener {
            val intentFavorite = Intent(context, MyFavoriteActivity::class.java)
            startActivity(intentFavorite)
        }
    }


    override fun onStart() {
        super.onStart()
        initialUserProfile()
        authUser.addAuthStateListener(authListener)

    }

    override fun onStop() {
        super.onStop()
        authUser.removeAuthStateListener(authListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_user_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
            R.id.logout -> {
                onShowDialogLogOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val objectUserProfile: String = "UserProfileFragment"
        fun newInstance(objectEvent: String): UserProfileFragment = UserProfileFragment().apply {
            arguments = Bundle().apply {
                putString(objectUserProfile, objectEvent)
            }
        }
    }
}
