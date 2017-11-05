package com.ipati.dev.castleevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.fragment.MyOrderFragment
import com.ipati.dev.castleevent.model.LoadingTicketsEvent

class MyOrderActivity : BaseAppCompatActivity(), LoadingTicketsEvent {
    private var myOrderFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_my_order, MyOrderFragment.newInstance(), tagMyOrderFragment)
                .commitNow()
    }

    override fun onShowTicketsUser(eventId: String, eventPhoto: String?, eventName: String
                                   , userAccount: String
                                   , eventLocation: String
                                   , count: Long) {

        myOrderFragment = supportFragmentManager.findFragmentByTag(tagMyOrderFragment)
        myOrderFragment?.let {
            (myOrderFragment as MyOrderFragment).onShowTicketsUserDialog(eventId, eventPhoto
                    , eventName
                    , userAccount, eventLocation
                    , count)
        }
    }

    companion object {
        private const val tagMyOrderFragment = "MyOrderFragment"
    }

}
