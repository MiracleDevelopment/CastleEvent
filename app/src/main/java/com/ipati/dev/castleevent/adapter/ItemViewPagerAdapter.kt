package com.ipati.dev.castleevent.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.fragment.LoginFragment
import com.ipati.dev.castleevent.fragment.UserProfileFragment
import com.ipati.dev.castleevent.service.AuthenticationStatus


class ItemViewPagerAdapter(supportFragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(supportFragmentManager) {
    private var mAuthentication: AuthenticationStatus = AuthenticationStatus()
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return ListEventFragment.newInstance("")
            }

            1 -> {
                return if (mAuthentication.getCurrentUser() != null) {
                    UserProfileFragment.newInstance("")
                } else {
                    LoginFragment.newInstance("")
                }
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }


}