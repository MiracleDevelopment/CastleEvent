package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.onShowToast
import com.ipati.dev.castleevent.service.FirebaseService.ListEventFavoriteRealTimeManager
import kotlinx.android.synthetic.main.activity_list_event_favorite_fragment.*

class ListEventFavoriteFragment : Fragment() {
    private lateinit var listEventFavoriteManager: ListEventFavoriteRealTimeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_list_event_favorite_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listEventFavoriteManager = ListEventFavoriteRealTimeManager("", lifecycle)
        setUpToolbar()
        setUpRecyclerView()

        arguments?.let {
            context.onShowToast(arguments.getString(stringCategoryObject))
        }
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_list_event_favorite)
            supportActionBar?.apply {
                title = ""
                setDisplayShowHomeEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setUpRecyclerView() {
        recycler_list_event_favorite.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_list_event_favorite.itemAnimator = DefaultItemAnimator()
        recycler_list_event_favorite.adapter = listEventFavoriteManager.adapterListFavoriteItem
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        private const val stringCategoryObject: String = "stringCategory"
        fun newInstance(stringCategory: String): ListEventFavoriteFragment = ListEventFavoriteFragment().apply {
            arguments = Bundle().apply {
                putString(stringCategoryObject, stringCategory)
            }
        }
    }
}
