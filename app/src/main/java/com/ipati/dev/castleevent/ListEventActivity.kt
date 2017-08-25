package com.ipati.dev.castleevent


import android.app.Dialog
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.model.Glide.loadPhotoProfileUser
import com.ipati.dev.castleevent.model.userManage.email
import com.ipati.dev.castleevent.model.userManage.photoUrl
import com.ipati.dev.castleevent.model.userManage.username
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseMenuListItem
import com.ipati.dev.castleevent.utill.manager.SharePreferenceManager
import kotlinx.android.synthetic.main.activity_list_event.*

class ListEventActivity : AppCompatActivity(), LifecycleRegistryOwner, View.OnClickListener {
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var realTimeDatabaseMenuList: RealTimeDatabaseMenuListItem
    private lateinit var mAlertDialog: AlertDialog.Builder
    private lateinit var dialog: Dialog
    private lateinit var sharePreferenceManager: SharePreferenceManager
    private var mLifeCycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        setSupportActionBar(toolbar_list_event)
        realTimeDatabaseMenuList = RealTimeDatabaseMenuListItem(applicationContext, this, lifecycle)
        sharePreferenceManager = SharePreferenceManager(context = applicationContext)
        initialToolbar()
        initialListItemMenu()
        initialLogOutButton()
        initialDefaultSetting()

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_list_event_layout
                        , ListEventFragment.newInstance("listEvent"))
                .commitNow()

    }


    private fun initialToolbar() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_list_event, toolbar_list_event,
                R.string.open_drawer_layout, R.string.close_drawer_layout)
        drawer_list_event.addDrawerListener(actionBarDrawerToggle)
    }

    private fun initialListItemMenu() {
        tv_name_user_list_event.text = username
        email_user_list_event.text = email
        loadPhotoProfileUser(applicationContext, photoUrl, im_profile_user_list_event)
        recycler_list_view_menu_list_event.apply {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recycler_list_view_menu_list_event.itemAnimator = DefaultItemAnimator()
            recycler_list_view_menu_list_event.adapter = realTimeDatabaseMenuList.adapterListItemMenu
        }
    }

    private fun initialLogOutButton() {
        tv_logout_button.setOnClickListener { view -> onClick(view) }
    }

    private fun initialDefaultSetting() {
        switch_notification.isChecked = sharePreferenceManager.defaultSharePreferenceNotificationManager()
        switch_language_thai.isChecked = sharePreferenceManager.defaultSharePreferenceLanguageManager()
        switch_notification.setOnCheckedChangeListener { compoundButton, i ->
            if (i) {
                sharePreferenceManager.sharePreferenceNotificationManager(i)
                compoundButton.isChecked = i
            } else {
                sharePreferenceManager.sharePreferenceNotificationManager(i)
                compoundButton.isChecked = i
            }
        }


        switch_language_thai.setOnCheckedChangeListener { compoundButton, i ->
            if (i) {
                sharePreferenceManager.sharePreferenceLanguageManager(i)
                compoundButton.isChecked = i
            } else {
                sharePreferenceManager.sharePreferenceLanguageManager(i)
                compoundButton.isChecked = i
            }
        }

    }

    private fun alertDialogExit(): Dialog {
        mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog.setCancelable(false)
        mAlertDialog.setTitle("คุณต้องการออกจากแอพนี้ใช่ / ไม่")
        mAlertDialog.setPositiveButton("exit", { dialogInterface, _ ->
            FirebaseAuth.getInstance().signOut()
            dialogInterface.dismiss()
            finish()
        })

        mAlertDialog.setNegativeButton("Cancel", { dialogInterface, _ ->
            dialogInterface.dismiss()
        })

        dialog = mAlertDialog.create()
        return dialog
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_logout_button -> {
                alertDialogExit().show()
            }
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifeCycleRegistry
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        alertDialogExit().show()
    }
}
