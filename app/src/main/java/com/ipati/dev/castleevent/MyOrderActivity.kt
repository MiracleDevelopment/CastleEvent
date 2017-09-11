package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ipati.dev.castleevent.fragment.MyOrderFragment
import com.ipati.dev.castleevent.model.LoadingTicketsEvent

class MyOrderActivity : AppCompatActivity(), LoadingTicketsEvent {
    private var myOrderFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_my_order, MyOrderFragment.newInstance(), "MyOrderFragment")
                .commitNow()
    }

    override fun onShowTicketsUser(eventId: String, userPhoto: String?, eventName: String, eventLogo: String, userAccount: String, eventLocation: String, count: Long) {
        myOrderFragment = supportFragmentManager.findFragmentByTag("MyOrderFragment")
        myOrderFragment?.let {
            (myOrderFragment as MyOrderFragment).onShowTicketsUserDialog(eventId, userPhoto, eventName, eventLogo, userAccount, eventLocation, count)
        }
    }

}
