package com.ipati.dev.castleevent.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.ipati.dev.castleevent.fragment.CalendarFragment
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.fragment.UserProfileFragment


class ItemViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            in 0..0 -> {
                return ListEventFragment.newInstance("")
            }
            in 1..1 -> {
                return UserProfileFragment.newInstance("")
            }

        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }
}