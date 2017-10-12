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
        overridePendingTransition(R.anim.enter_anim_slide_left, 0)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.exit_anim_slide_left)
    }
}
