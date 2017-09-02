package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ipati.dev.castleevent.fragment.RegisterFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_register
                        , RegisterFragment.newInstance())
                .commitNow()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
