package com.ipati.dev.castleevent.fragment


import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ListMyOrderAdapter
import com.ipati.dev.castleevent.service.FirebaseService.MyOrderRealTimeManager
import kotlinx.android.synthetic.main.activity_my_order_fragment.*

class MyOrderFragment : Fragment(), LifecycleRegistryOwner {
    private lateinit var mMyOrderRealTimeManager: MyOrderRealTimeManager
    private var mRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mMyOrderRealTimeManager = MyOrderRealTimeManager(context, lifecycle)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_my_order_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialToolbar()
        initialRecyclerView()
    }

    private fun initialToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_my_order)
            title = ""
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mRegistry
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return true
    }

    private fun initialRecyclerView() {
        recycler_view_my_order.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }
        recycler_view_my_order.adapter = mMyOrderRealTimeManager.mAdapterMyOrder
    }

    companion object {
        fun newInstance(): MyOrderFragment {
            return MyOrderFragment().apply {
                arguments = Bundle()
            }
        }
    }
}
