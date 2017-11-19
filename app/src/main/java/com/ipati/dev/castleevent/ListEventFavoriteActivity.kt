package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.extension.replaceFragment
import com.ipati.dev.castleevent.fragment.ListEventFavoriteFragment

class ListEventFavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event_favorite)

        val stringCategory: String? = intent.extras.getString("stringCategory")
        stringCategory?.let {
            supportFragmentManager.replaceFragment(R.id.frame_layout_list_event_favorite_fragment
                    , ListEventFavoriteFragment.newInstance(stringCategory)
                    , "ListEventFavoriteFragment")
        } ?: supportFinishAfterTransition()
    }
}
