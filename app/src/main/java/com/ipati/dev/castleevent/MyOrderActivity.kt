package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.fragment.MyOrderFragment

class MyOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_my_order, MyOrderFragment.newInstance())
                .commitNow()
    }
}
