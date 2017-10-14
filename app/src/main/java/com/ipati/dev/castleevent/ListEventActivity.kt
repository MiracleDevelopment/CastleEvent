package com.ipati.dev.castleevent

import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.SharedElementCallback
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.transition.Transition
import android.util.Log
import android.view.*
import android.widget.Toast
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.DraweeTransition
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.ipati.dev.castleevent.adapter.ItemViewPagerAdapter
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.model.OnLogOutSystem
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.uid
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseMenuListItem
import com.ipati.dev.castleevent.service.googleApiClient
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_list_event.*
import kotlinx.android.synthetic.main.bottom_navigation_layout.*

class ListEventActivity : AppCompatActivity(), View.OnClickListener, OnLogOutSystem {
    private lateinit var realTimeDatabaseMenuList: RealTimeDatabaseMenuListItem
    private lateinit var mItemViewPagerAdapter: ItemViewPagerAdapter
    private lateinit var sharePreferenceManager: SharePreferenceSettingManager
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mLifeCycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var doubleTwiceBackPress: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        mAuth = FirebaseAuth.getInstance()
        mAuth.addAuthStateListener(onChangeStateLogin())

        realTimeDatabaseMenuList = RealTimeDatabaseMenuListItem(applicationContext, lifecycle)
        sharePreferenceManager = SharePreferenceSettingManager(context = applicationContext)
        onChangeStateLogin()
        initialViewPager()
        initialBottomNavigationBar()

        Log.d("tokenFireBase", FirebaseInstanceId.getInstance().token.toString())
    }

    private fun onChangeStateLogin(): FirebaseAuth.AuthStateListener {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth? ->
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

        return mAuthListener
    }


    private fun initialViewPager() {
        mItemViewPagerAdapter = ItemViewPagerAdapter(supportFragmentManager)
        vp_list_event.adapter = mItemViewPagerAdapter

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
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.itemCategory -> {
                    vp_list_event.currentItem = 0
                    bottom_navigation_list_event.menu.getItem(vp_list_event.currentItem).isChecked = true

                    val mListEventFragment: Fragment? = mItemViewPagerAdapter.getRegisteredFragment(vp_list_event.currentItem)
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

                    val mListEventFragment: Fragment? = mItemViewPagerAdapter.getRegisteredFragment(0)
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

    override fun logOutApplication() {
        FirebaseAuth.getInstance().signOut()
        initialViewPager()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment: Fragment? = mItemViewPagerAdapter.getRegisteredFragment(vp_list_event.currentItem)
        fragment?.let {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {

        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifeCycleRegistry
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthListener)
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
