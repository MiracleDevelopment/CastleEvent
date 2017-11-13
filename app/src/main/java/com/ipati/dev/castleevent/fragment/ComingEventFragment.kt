package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.service.FirebaseService.ComingRealTimeDatabaseManager
import kotlinx.android.synthetic.main.activity_comming_event_fragment.*

class ComingEventFragment : Fragment() {

    private lateinit var comingRealTimeDatabaseManager: ComingRealTimeDatabaseManager
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_comming_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        comingRealTimeDatabaseManager = ComingRealTimeDatabaseManager(lifecycle)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recycler_coming_event.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_coming_event.itemAnimator = DefaultItemAnimator()
        recycler_coming_event.adapter = comingRealTimeDatabaseManager.adapterListComing
    }

    companion object {
        fun newInstance(): ComingEventFragment = ComingEventFragment().apply { arguments = Bundle() }
    }
}
