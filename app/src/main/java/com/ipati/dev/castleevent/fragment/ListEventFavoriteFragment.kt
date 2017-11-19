package com.ipati.dev.castleevent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.ListDetailEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.service.FirebaseService.ListEventFavoriteRealTimeManager
import kotlinx.android.synthetic.main.activity_list_event_favorite_fragment.*

class ListEventFavoriteFragment : BaseFragment() {
    private lateinit var listEventFavoriteManager: ListEventFavoriteRealTimeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.activity_list_event_favorite_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(arguments.getString(stringCategoryObject))
        setUpRecyclerView()
        arguments?.let {
            when (arguments.getString(stringCategoryObject)) {
                "การศึกษา" -> {
                    setUpListFavoriteManager("Education")
                }
                "ดนตรี" -> {
                    setUpListFavoriteManager("Music")
                }
                "เทคโนโลยี" -> {
                    setUpListFavoriteManager("Technology")
                }
                "สัตว์เลี้ยง" -> {
                    setUpListFavoriteManager("Animal")
                }
                "งานเลี้ยง" -> {
                    setUpListFavoriteManager("Party")
                }
                "อาหาร" -> {
                    setUpListFavoriteManager("Food")
                }
                "ศิลปะ" -> {
                    setUpListFavoriteManager("Art")
                }
                "เพลง" -> {
                    setUpListFavoriteManager("Music")
                }
                else -> {
                    setUpListFavoriteManager(arguments.getString(stringCategoryObject))
                }
            }
        }
    }

    private fun setUpToolbar(stringCategory: String) {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_list_event_favorite)
            supportActionBar?.apply {
                title = stringCategory
                setDisplayShowHomeEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setUpRecyclerView() {
        recycler_list_event_favorite.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_list_event_favorite.itemAnimator = DefaultItemAnimator()
    }

    private fun setUpListFavoriteManager(categoryName: String) {
        listEventFavoriteManager = ListEventFavoriteRealTimeManager(categoryName, lifecycle)
        recycler_list_event_favorite.adapter = listEventFavoriteManager.adapterListFavoriteItem
        listEventFavoriteManager.adapterListFavoriteItem.callBackItemCount = { itemCount ->
            when (itemCount) {
                0 -> {
                    tv_no_item_favorite_list.visibility = View.VISIBLE
                }

                else -> {
                    tv_no_item_favorite_list.visibility = View.GONE
                }
            }
        }

        listEventFavoriteManager.adapterListFavoriteItem.callBackClickableItem = { view, width, height, transitionName, eventId, statusId ->
            val intentDetailEvent = Intent(context, ListDetailEventActivity::class.java)
            val activityOptionCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity
                    , view
                    , ViewCompat.getTransitionName(view))

            intentDetailEvent.putExtra("eventId", eventId)
            intentDetailEvent.putExtra("width", width)
            intentDetailEvent.putExtra("height", height)
            intentDetailEvent.putExtra("transitionName", transitionName)
            intentDetailEvent.putExtra("status", statusId)
            startActivity(intentDetailEvent, activityOptionCompat.toBundle())
        }
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
