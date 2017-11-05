package com.ipati.dev.castleevent.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.ipati.dev.castleevent.fragment.ListEventFragment


class ItemViewPagerAdapter(supportFragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(supportFragmentManager) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return ListEventFragment.newInstance("")
            }

        }
        return null
    }

    override fun getCount(): Int = 1


}