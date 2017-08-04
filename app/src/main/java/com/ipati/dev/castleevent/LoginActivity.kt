package com.ipati.dev.castleevent

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultBus
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultEvent
import com.ipati.dev.castleevent.fragment.loading.LoadingFragment
import com.ipati.dev.castleevent.fragment.LoginFragment
import com.ipati.dev.castleevent.model.LoadingListener
import com.ipati.dev.castleevent.model.ShowListEventFragment

class LoginActivity : AppCompatActivity(), LoadingListener, ShowListEventFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame_login_fragment
                , LoginFragment.newInstance("LoginFragment")).commitNow()

    }

    override fun onShowLoading(statusLoading: Boolean) {
        supportFragmentManager.beginTransaction().add(R.id.frame_login_fragment
                , LoadingFragment.newInstance(statusLoading), "LoadingFragment").commitNow()
    }

    override fun onHindLoading(statusLoading: Boolean) {
        val loadingFragment: Fragment = supportFragmentManager.findFragmentByTag("LoadingFragment")
        supportFragmentManager.beginTransaction()
                .remove(loadingFragment).commitNow()
    }

    override fun onShowListFragment() {
        val listEventIntent: Intent = Intent(applicationContext, ListEventActivity::class.java)
        startActivity(listEventIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ActivityResultBus.getInstance().postQueue(ActivityResultEvent(requestCode, resultCode, data))
    }


}
