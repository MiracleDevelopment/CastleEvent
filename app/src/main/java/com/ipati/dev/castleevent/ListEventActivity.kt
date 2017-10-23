package com.ipati.dev.castleevent

import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.ipati.dev.castleevent.adapter.ItemViewPagerAdapter
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.model.OnChangeNotificationChannel
import com.ipati.dev.castleevent.model.OnCustomLanguage
import com.ipati.dev.castleevent.model.OnLogOutSystem
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username
import com.ipati.dev.castleevent.service.FirebaseNotification.NotificationManager
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseMenuListItem
import com.ipati.dev.castleevent.service.googleApiClient
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_list_event.*
import kotlinx.android.synthetic.main.bottom_navigation_layout.*


class ListEventActivity : BaseAppCompatActivity(), View.OnClickListener, OnLogOutSystem, OnCustomLanguage, OnChangeNotificationChannel {
    private lateinit var realTimeDatabaseMenuList: RealTimeDatabaseMenuListItem
    private lateinit var itemViewPagerAdapter: ItemViewPagerAdapter
    private lateinit var sharePreferenceManager: SharePreferenceSettingManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private var lifeCycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var doubleTwiceBackPress: Boolean = false

    private val handlerThread: Handler by lazy {
        Handler()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener(onChangeStateLogin())

        realTimeDatabaseMenuList = RealTimeDatabaseMenuListItem(applicationContext, lifecycle)
        sharePreferenceManager = SharePreferenceSettingManager(context = applicationContext)
        notificationManager = NotificationManager(this)

        onChangeStateLogin()
        initialViewPager()
        initialBottomNavigationBar()

        Log.d("tokenFireBase", FirebaseInstanceId.getInstance().token.toString())
    }

    private fun onChangeStateLogin(): FirebaseAuth.AuthStateListener {
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth? ->
            firebaseAuth?.let {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                firebaseUser?.let {
                    uid = firebaseUser.uid
                    username = firebaseUser.displayName
                    userEmail = firebaseUser.email
                    photoUrl = firebaseUser.photoUrl.toString()
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
                when (position) {
                    in 0..0 -> {
                        bottom_navigation_list_event.selectedItemId = R.id.itemListEvent
                    }
                    in 1..1 -> {
                        bottom_navigation_list_event.selectedItemId = R.id.itemUser
                    }
                }
            }
        })
    }

    private fun initialBottomNavigationBar() {
        bottom_navigation_list_event.inflateMenu(R.menu.menu_bottom_navigation_layout)
        bottom_navigation_list_event.itemTextColor = ContextCompat.getColorStateList(applicationContext, R.color.custom_selector_navigation_bottom)
        bottom_navigation_list_event.itemIconTintList = ContextCompat.getColorStateList(applicationContext, R.color.custom_selector_navigation_bottom)
        bottom_navigation_list_event.selectedItemId = R.id.itemListEvent

        bottom_navigation_list_event.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemListEvent -> {
                    vp_list_event.currentItem = 0
                    val listEventFragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(vp_list_event.currentItem)
                    listEventFragment?.let {
                        (listEventFragment as ListEventFragment).apply {
                            onDisableBottomSheetCategory()
                        }
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.itemCategory -> {
                    vp_list_event.currentItem = 0
                    bottom_navigation_list_event.menu.getItem(vp_list_event.currentItem).isChecked = true

                    val mListEventFragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(vp_list_event.currentItem)
                    mListEventFragment?.let {
                        (mListEventFragment as ListEventFragment).apply {
                            onShowBottomSheetCategory()
                        }
                    }
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.itemUser -> {
                    vp_list_event.currentItem = 1
                    bottom_navigation_list_event.menu.getItem(vp_list_event.currentItem).isChecked = true

                    val mListEventFragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(0)
                    mListEventFragment?.let {
                        (mListEventFragment as ListEventFragment).apply {
                            onDisableBottomSheetCategory()
                        }
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }


    }


    override fun onChangeLanguage(language: Int) {
        when (language) {
            0 -> {
                setLanguage("th")
            }

            1 -> {
                setLanguage("en")
            }
        }
    }

    override fun onChangeNotification(notification: Int) {
        when (notification) {
            0 -> {
                notificationManager.cancelNotification()
            }

            1 -> {
                notificationManager.activeNotification()
            }
        }

    }


    override fun logOutApplication() {
        FirebaseAuth.getInstance().signOut()

        recreate()
        initialViewPager()
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
