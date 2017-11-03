package com.ipati.dev.castleevent

import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ipati.dev.castleevent.adapter.ItemViewPagerAdapter
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.extension.onShowSettingDialog
import com.ipati.dev.castleevent.extension.onShowSnackBar
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username
import com.ipati.dev.castleevent.service.FirebaseNotification.NotificationManager
import com.ipati.dev.castleevent.service.googleApiClient
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_list_event.*


class ListEventActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var itemViewPagerAdapter: ItemViewPagerAdapter
    private lateinit var sharePreferenceManager: SharePreferenceSettingManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private var lifeCycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var doubleTwiceBackPress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        auth = FirebaseAuth.getInstance()
        sharePreferenceManager = SharePreferenceSettingManager(context = applicationContext)
        notificationManager = NotificationManager(this)

        onChangeStateLogin()
        initialViewPager()
        setUpTabLayout()
        setUpDrawerSimpleProfile()
        setUpDrawerSetting()

    }

    private fun setUpDrawerSetting() {
        im_setting_list_event.setOnClickListener {

            val settingDialogFragment = onShowSettingDialog(supportFragmentManager)
            settingDialogFragment.onChangeLanguage = { status ->
                if (status) {
                    setLanguage("en")
                } else {
                    setLanguage("th")
                }
            }

            settingDialogFragment.onChangeNotification = { status ->
                if (status) {
                    onShowSnackBar(drawer_list_event, "เปิดการแจ้งเตือน")
                } else {
                    onShowSnackBar(drawer_list_event, "ปิดการแจ้งเตือน")
                }
            }
        }
    }

    private fun setUpDrawerSimpleProfile() {
        drawee_user_login.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val intentUserProfile = Intent(this, UserProfileMenuActivity::class.java)
                startActivity(intentUserProfile)
            } else {
                val intentUserLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentUserLogin)
            }
        }
    }

    private fun setUpTabLayout() {
        tab_layout_list_event.addTab(tab_layout_list_event.newTab().setText("New"), 0, true)
        tab_layout_list_event.addTab(tab_layout_list_event.newTab().setText("Coming"), 1)
        tab_layout_list_event.addTab(tab_layout_list_event.newTab().setText("expire"), 2)
    }

    private fun onChangeStateLogin(): FirebaseAuth.AuthStateListener {
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth? ->
            firebaseAuth?.let {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                if (firebaseUser != null) {
                    uid = firebaseUser.uid
                    username = firebaseUser.displayName
                    userEmail = firebaseUser.email
                    photoUrl = firebaseUser.photoUrl.toString()

                    drawee_user_login.hierarchy.setPlaceholderImage(R.mipmap.ic_launcher)
                    drawee_user_login.setImageURI(photoUrl, applicationContext)
                } else {
                    drawee_user_login.hierarchy.setPlaceholderImage(null)
                    drawee_user_login.setImageURI("", applicationContext)
                }
            }
        }
        return authListener
    }


    private fun initialViewPager() {
        itemViewPagerAdapter = ItemViewPagerAdapter(supportFragmentManager)
        vp_list_event.adapter = itemViewPagerAdapter

        vp_list_event.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(vp_list_event.currentItem)
        fragment?.let {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {

        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifeCycleRegistry
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onBackPressed() {
        if (doubleTwiceBackPress) {
            super.onBackPressed()
            FirebaseAuth.getInstance().signOut()
            googleApiClient?.disconnect()
            supportFinishAfterTransition()
            return
        }

        doubleTwiceBackPress = true
        Toast.makeText(applicationContext, "Press Back Once Again", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            doubleTwiceBackPress = false
        }, 2000)

    }
}
