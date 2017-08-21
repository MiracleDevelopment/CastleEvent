package com.ipati.dev.castleevent.fragment

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_list_event_fragment.*

class ListEventFragment : Fragment(), LifecycleRegistryOwner {
    private var mRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private lateinit var realTimeDatabaseManager: RealTimeDatabaseManager
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeDatabaseManager = RealTimeDatabaseManager(context, lifecycle)
        activity.invalidateOptionsMenu()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_list_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialToolbar()
        initialRecyclerView()
    }

    private fun initialToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar_list_event_fragment)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        actionBarDrawerToggle = ActionBarDrawerToggle(activity, drawer_list_event_fragment, toolbar_list_event_fragment, R.string.open_drawer_layout, R.string.close_drawer_layout)
        drawer_list_event_fragment.addDrawerListener(actionBarDrawerToggle)

    }

    private fun initialRecyclerView() {
        recycler_list_event.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_list_event.itemAnimator = DefaultItemAnimator()
        recycler_list_event.adapter = realTimeDatabaseManager.adapterListEvent
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }


    override fun getLifecycle(): LifecycleRegistry {
        return this.mRegistry
    }

    companion object {
        private var listEventObject: String = "ListEventFragment"
        fun newInstance(nameObject: String): ListEventFragment {
            val listEventFragment = ListEventFragment()
            val bundle = Bundle()
            bundle.putString(listEventObject, nameObject)
            listEventFragment.arguments = bundle
            return listEventFragment
        }
    }
}
