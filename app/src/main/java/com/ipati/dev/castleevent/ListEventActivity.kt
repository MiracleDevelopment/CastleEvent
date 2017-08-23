package com.ipati.dev.castleevent

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseMenuListItem
import kotlinx.android.synthetic.main.activity_list_event.*

class ListEventActivity : AppCompatActivity(), LifecycleRegistryOwner {
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var realTimeDatabaseMenuList: RealTimeDatabaseMenuListItem
    private var mLifeCycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        setSupportActionBar(toolbar_list_event)
        realTimeDatabaseMenuList = RealTimeDatabaseMenuListItem(context = applicationContext, lifecycle = lifecycle)
        initialToolbar()
        initialListItemMenu()

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_list_event_layout
                        , ListEventFragment.newInstance("listEvent"))
                .commitNow()

    }

    private fun initialListItemMenu() {
        recycler_list_view_menu_list_event.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_list_view_menu_list_event.itemAnimator = DefaultItemAnimator()

        recycler_list_view_menu_list_event.adapter = realTimeDatabaseMenuList.adapterListItemMenu
    }

    private fun initialToolbar() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_list_event, toolbar_list_event,
                R.string.open_drawer_layout, R.string.close_drawer_layout)
        drawer_list_event.addDrawerListener(actionBarDrawerToggle)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifeCycleRegistry
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
