package com.ipati.dev.castleevent

import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.adapter.ItemViewPagerAdapter
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseMenuListItem
import com.ipati.dev.castleevent.service.googleApiClient
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_list_event.*
import kotlinx.android.synthetic.main.bottom_navigation_layout.*

class ListEventActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var realTimeDatabaseMenuList: RealTimeDatabaseMenuListItem
    private lateinit var mItemViewPagerAdapter: ItemViewPagerAdapter
    private lateinit var sharePreferenceManager: SharePreferenceSettingManager
    private var mLifeCycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        setSupportActionBar(toolbar_list_event)
        supportActionBar?.setLogo(R.mipmap.ic_launcher_event)
        realTimeDatabaseMenuList = RealTimeDatabaseMenuListItem(applicationContext, lifecycle)
        sharePreferenceManager = SharePreferenceSettingManager(context = applicationContext)
        initialViewPager()
        initialBottomNavigationBar()

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
        bottom_navigation_list_event.itemTextColor = ContextCompat.getColorStateList(applicationContext, R.color.colorItemTextBottomNavigation)
        bottom_navigation_list_event.itemIconTintList = ContextCompat.getColorStateList(applicationContext, R.color.colorItemIconBottomNavigation)
        bottom_navigation_list_event.selectedItemId = R.id.itemListEvent

        bottom_navigation_list_event.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemListEvent -> {
                    vp_list_event.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.itemCategory -> {
                    Toast.makeText(applicationContext, "Category", Toast.LENGTH_SHORT).show()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.itemUser -> {
                    vp_list_event.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifeCycleRegistry
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_login -> {
                FirebaseAuth.getInstance().signOut()
                finish()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseAuth.getInstance().signOut()
        googleApiClient?.disconnect()
    }
}
