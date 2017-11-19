package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.extension.replaceFragment
import com.ipati.dev.castleevent.fragment.MyFavoriteFragment

class MyFavoriteActivity : BaseAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite)

        supportFragmentManager.replaceFragment(R.id.frame_my_favorite_fragment
                , MyFavoriteFragment.newInstance(), questionFragmentObject)
    }

    companion object {
        private const val questionFragmentObject = "QuestionFragment"
    }
}
