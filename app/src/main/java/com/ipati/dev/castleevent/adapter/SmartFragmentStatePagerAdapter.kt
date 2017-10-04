package com.ipati.dev.castleevent.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.ViewGroup
import android.util.SparseArray
import android.support.v4.app.FragmentStatePagerAdapter
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.fragment.UserProfileFragment

abstract class SmartFragmentStatePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val registeredFragments = SparseArray<Fragment>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }


    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments[position]
    }
}