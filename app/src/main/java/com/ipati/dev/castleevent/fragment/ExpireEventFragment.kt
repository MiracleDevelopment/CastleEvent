package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ExpireListEventAdapter
import com.ipati.dev.castleevent.service.FirebaseService.ExpireRealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_expire_event_fragment.*

class ExpireEventFragment : Fragment() {
    private lateinit var expireRealTimeDatabase: ExpireRealTimeDatabaseManager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_expire_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expireRealTimeDatabase = ExpireRealTimeDatabaseManager(lifecycle)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recycler_expire_event.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_expire_event.itemAnimator = DefaultItemAnimator()
        recycler_expire_event.adapter = expireRealTimeDatabase.adapterExpire

    }

    companion object {
        fun newInstance(): ExpireEventFragment = ExpireEventFragment().apply { arguments = Bundle() }
    }
}
