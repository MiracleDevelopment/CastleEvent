package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.extension.replaceFragment
import com.ipati.dev.castleevent.fragment.FavoriteCategoryFragment

class FavoriteCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_category)

        val statusBoolean = intent.extras.getBoolean(keyStatus)
        supportFragmentManager.replaceFragment(R.id.frame_favorite_category_fragment
                , FavoriteCategoryFragment.newInstance(statusBoolean), favoriteCategoryFragment)
    }

    companion object {
        private const val favoriteCategoryFragment = "FavoriteCategoryFragment"
        private const val keyStatus = "status"
    }
}
