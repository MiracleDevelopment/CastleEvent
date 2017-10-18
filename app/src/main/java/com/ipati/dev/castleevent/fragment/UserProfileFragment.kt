package com.ipati.dev.castleevent.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.fragment.loading.LogOutFragmentDialog
import com.ipati.dev.castleevent.model.OnChangeNotificationChannel
import com.ipati.dev.castleevent.model.Fresco.loadPhotoProfileUser
import com.ipati.dev.castleevent.model.OnCustomLanguage
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseMenuListItem
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_user_profile_fragment.*

class UserProfileFragment : BaseFragment() {
    private lateinit var realTimeDatabaseMenuListItem: RealTimeDatabaseMenuListItem
    private lateinit var sharePreferenceSettingMenuList: SharePreferenceSettingManager
    private lateinit var onChangeLanguage: OnCustomLanguage
    private lateinit var onChangeNotification: OnChangeNotificationChannel
    private lateinit var logOutDialogFragment: LogOutFragmentDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realTimeDatabaseMenuListItem = RealTimeDatabaseMenuListItem(context, lifecycle)
        sharePreferenceSettingMenuList = SharePreferenceSettingManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_user_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialRecyclerMenu()
        initialSwitchSetting()
        initialLogOutBt()
    }

    private fun initialLogOutBt() {
        tv_logout_system.setOnClickListener {
            logOutDialogFragment = LogOutFragmentDialog.newInstance("กรุณายืนยัน", "คุณต้องการออกจากระบบ ใช่ / ไม่")
            logOutDialogFragment.isCancelable = false
            logOutDialogFragment.show(activity.supportFragmentManager, "LogOutDialogFragment")
        }
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
        switch_language_user_profile.isChecked = sharePreferenceSettingMenuList.defaultSharePreferenceLanguageManager()!!
        switch_notification_user_profile.isChecked = sharePreferenceSettingMenuList.defaultSharePreferenceNotificationManager()!!

        switch_language_user_profile.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                compoundButton.isChecked = b
                sharePreferenceSettingMenuList.sharePreferenceLanguageManager(b)

                onChangeLanguage = activity as ListEventActivity
                onChangeLanguage.onChangeLanguage(1)

            } else {
                compoundButton.isChecked = b
                sharePreferenceSettingMenuList.sharePreferenceLanguageManager(b)

                onChangeLanguage = activity as ListEventActivity
                onChangeLanguage.onChangeLanguage(0)
            }
        }

        switch_notification_user_profile.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                compoundButton.isChecked = b
                sharePreferenceSettingMenuList.sharePreferenceNotificationManager(b)

                onChangeNotification = activity as ListEventActivity
                onChangeNotification.onChangeNotification(1)
                Toast.makeText(context, "active", Toast.LENGTH_SHORT).show()
            } else {
                compoundButton.isChecked = b
                sharePreferenceSettingMenuList.sharePreferenceNotificationManager(b)

                onChangeNotification = activity as ListEventActivity
                onChangeNotification.onChangeNotification(0)
                Toast.makeText(context, "unActive", Toast.LENGTH_SHORT).show()
            }
        }

        sharePreferenceSettingMenuList.defaultSharePreferenceLanguageManager()?.let {
            if (sharePreferenceSettingMenuList.defaultSharePreferenceNotificationManager()!!) {
                onChangeNotification = activity as ListEventActivity
                onChangeNotification.onChangeNotification(1)
            } else {
                onChangeNotification = activity as ListEventActivity
                onChangeNotification.onChangeNotification(0)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        initialUserProfile()

    }


    companion object {
        private var objectUserProfile: String = "UserProfileFragment"
        fun newInstance(objectEvent: String): UserProfileFragment {
            return UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(objectUserProfile, objectEvent)
                }
            }
        }
    }
}
