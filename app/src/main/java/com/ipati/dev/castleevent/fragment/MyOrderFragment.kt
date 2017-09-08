package com.ipati.dev.castleevent.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.adapter.ListMyOrderAdapter
import kotlinx.android.synthetic.main.activity_my_order_fragment.*

class MyOrderFragment : Fragment() {
    lateinit var mListMyOrderAdapter: ListMyOrderAdapter
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

    private fun initialRecyclerView() {
        recycler_view_my_order.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }
        mListMyOrderAdapter = ListMyOrderAdapter(context)
        recycler_view_my_order.adapter = mListMyOrderAdapter
    }

    companion object {
        fun newInstance(): MyOrderFragment {
            return MyOrderFragment().apply {
                arguments = Bundle()
            }
        }
    }
}
