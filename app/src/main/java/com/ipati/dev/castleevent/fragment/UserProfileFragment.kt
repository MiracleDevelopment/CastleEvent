package com.ipati.dev.castleevent.fragment

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Glide.loadPhotoProfileUser
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.userEmail
import com.ipati.dev.castleevent.model.userManage.username
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseMenuListItem
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_user_profile_fragment.*

class UserProfileFragment : Fragment(), LifecycleRegistryOwner {
    private lateinit var realTimeDatabaseMenuListItem: RealTimeDatabaseMenuListItem
    private lateinit var mSharePreferenceSettingMenuList: SharePreferenceSettingManager
    private var mRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeDatabaseMenuListItem = RealTimeDatabaseMenuListItem(context, lifecycle)
        mSharePreferenceSettingMenuList = SharePreferenceSettingManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_user_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialUserProfile()
        initialRecyclerMenu()
        initialSwitchSetting()
    }

    private fun initialUserProfile() {
        loadPhotoProfileUser(context, photoUrl, im_user_profile_photo)

        tv_user_profile_name_full.text = username
        tv_user_profile_email.text = userEmail
    }

    private fun initialRecyclerMenu() {
        user_profile_list_menu.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        user_profile_list_menu.itemAnimator = DefaultItemAnimator()

        user_profile_list_menu.adapter = realTimeDatabaseMenuListItem.adapterListItemMenu
    }

    private fun initialSwitchSetting() {
        switch_language_user_profile.isChecked = mSharePreferenceSettingMenuList.defaultSharePreferenceLanguageManager()
        switch_notification_user_profile.isChecked = mSharePreferenceSettingMenuList.defaultSharePreferenceNotificationManager()

        switch_language_user_profile.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                compoundButton.isChecked = b
                mSharePreferenceSettingMenuList.sharePreferenceLanguageManager(b)
            } else {
                compoundButton.isChecked = b
                mSharePreferenceSettingMenuList.sharePreferenceLanguageManager(b)
            }
        }

        switch_notification_user_profile.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                compoundButton.isChecked = b
                mSharePreferenceSettingMenuList.sharePreferenceNotificationManager(b)
            } else {
                compoundButton.isChecked = b
                mSharePreferenceSettingMenuList.sharePreferenceNotificationManager(b)
            }
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mRegistry
    }

    companion object {
        var objectUserProfile: String = "UserProfileFragment"
        fun newInstance(objectEvent: String): UserProfileFragment {
            return UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(objectUserProfile, objectEvent)
                }
            }
        }
    }
}