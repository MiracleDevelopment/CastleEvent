package com.ipati.dev.castleevent

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultBus
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultEvent
import com.ipati.dev.castleevent.fragment.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame_login_fragment
                , LoginFragment.newInstance("LoginFragment")).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ActivityResultBus.getInstance().postQueue(ActivityResultEvent(requestCode, resultCode, data))
    }
}
