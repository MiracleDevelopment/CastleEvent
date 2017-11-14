package com.ipati.dev.castleevent.adapter

import android.content.Context
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.fragment.ComingEventFragment
import com.ipati.dev.castleevent.fragment.ExpireEventFragment
import com.ipati.dev.castleevent.fragment.ListEventFragment

@Keep
class ItemViewPagerAdapter(context: Context, supportFragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(supportFragmentManager) {
    private val contextViewPager: Context = context

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return ListEventFragment.newInstance("")
            }
            1 -> {
                return ComingEventFragment.newInstance()
            }
            2 -> {
                return ExpireEventFragment.newInstance()
            }
        }
        return null
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> {
                return contextViewPager.getString(R.string.news_topic)
            }
            1 -> {
                return contextViewPager.getString(R.string.coming_topic)
            }
            2 -> {
                return contextViewPager.getString(R.string.expire_topic)
            }
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int = 3


}