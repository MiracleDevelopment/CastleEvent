package com.ipati.dev.castleevent

import android.arch.lifecycle.LifecycleRegistry
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ipati.dev.castleevent.adapter.ItemViewPagerAdapter
import com.ipati.dev.castleevent.base.BaseAppCompatActivity
import com.ipati.dev.castleevent.extension.onShowSettingDialog
import com.ipati.dev.castleevent.extension.onShowSnackBar
import com.ipati.dev.castleevent.extension.onShowToast
import com.ipati.dev.castleevent.fragment.ComingEventFragment
import com.ipati.dev.castleevent.fragment.ExpireEventFragment
import com.ipati.dev.castleevent.fragment.ListEventFragment
import com.ipati.dev.castleevent.model.LoadingCategory
import com.ipati.dev.castleevent.model.UserManager.photoUrl
import com.ipati.dev.castleevent.model.UserManager.uid
import com.ipati.dev.castleevent.model.UserManager.userEmail
import com.ipati.dev.castleevent.model.UserManager.username
import com.ipati.dev.castleevent.service.FirebaseNotification.NotificationManager
import com.ipati.dev.castleevent.service.FirebaseService.CategoryRealTimeManager
import com.ipati.dev.castleevent.service.googleApiClient
import com.ipati.dev.castleevent.utill.SharePreferenceSettingManager
import kotlinx.android.synthetic.main.activity_list_event.*
import kotlinx.android.synthetic.main.custom_bottom_sheet_category.*

class ListEventActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var itemViewPagerAdapter: ItemViewPagerAdapter
    private lateinit var sharePreferenceManager: SharePreferenceSettingManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var categoryManager: CategoryRealTimeManager
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var lifeCycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var doubleTwiceBackPress: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        auth = FirebaseAuth.getInstance()
        sharePreferenceManager = SharePreferenceSettingManager(context = applicationContext)
        categoryManager = CategoryRealTimeManager(this, lifecycle)
        notificationManager = NotificationManager(this)

        setChangeStateLogin()
        setUpTabLayout()
        initialViewPager()
        setUpBottomSheet()
        setUpRecyclerBottomSheet()
        setUpDrawerSimpleProfile()
        setUpDrawerSetting()
        setUpSearchEvent()
    }


    private fun setUpBottomSheet() {
        im_header_bottom_sheet_category.setOnClickListener(this)
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_category)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
    }

    private fun setUpDrawerSetting() {
        im_setting_list_event.setOnClickListener {
            val settingDialogFragment = onShowSettingDialog(supportFragmentManager)
            settingDialogFragment.onChangeLanguage = { status ->
                if (status) {
                    setLanguage("en")
                } else {
                    setLanguage("th")
                }
            }

            settingDialogFragment.onChangeNotification = { status ->
                if (status) {
                    onShowSnackBar(drawer_list_event, "เปิดการแจ้งเตือน")
                } else {
                    onShowSnackBar(drawer_list_event, "ปิดการแจ้งเตือน")
                }
            }
        }
    }

    private fun setUpDrawerSimpleProfile() {
        drawee_user_login.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val intentUserProfile = Intent(this, UserProfileMenuActivity::class.java)
                startActivity(intentUserProfile)
            } else {
                val intentUserLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentUserLogin)
            }
        }
    }

    private fun setUpTabLayout() {
        tab_layout_list_event.setupWithViewPager(vp_list_event)
    }

    private fun setChangeStateLogin(): FirebaseAuth.AuthStateListener {
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth? ->
            firebaseAuth?.let {
                val fireBaseUser: FirebaseUser? = firebaseAuth.currentUser
                if (fireBaseUser != null) {
                    uid = fireBaseUser.uid
                    username = fireBaseUser.displayName
                    userEmail = fireBaseUser.email
                    photoUrl = fireBaseUser.photoUrl.toString()

                    drawee_user_login.hierarchy.setPlaceholderImage(R.mipmap.castle_place_holder)
                    drawee_user_login.setImageURI(photoUrl, applicationContext)
                } else {
                    drawee_user_login.hierarchy.setPlaceholderImage(null)
                    drawee_user_login.setImageURI("", applicationContext)
                    drawee_user_login.setActualImageResource(R.mipmap.ic_person_white)
                }
            }
        }
        return authListener
    }


    private fun initialViewPager() {
        itemViewPagerAdapter = ItemViewPagerAdapter(applicationContext, supportFragmentManager)
        vp_list_event.offscreenPageLimit = 3
        vp_list_event.adapter = itemViewPagerAdapter
        vp_list_event.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                ed_search_filter.setText("")
                ed_search_filter.isCursorVisible = false
                ed_search_filter.removeTextChangedListener(textWatcher)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })
    }

    private fun setUpRecyclerBottomSheet() {
        recycler_bottom_sheet.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recycler_bottom_sheet.itemAnimator = DefaultItemAnimator()
        recycler_bottom_sheet.adapter = categoryManager.categoryAdapter

        categoryManager.categoryAdapter.setOnChangeCategory(object : LoadingCategory {
            override fun setOnChangeCategory(selectCategory: String) {
                ed_search_filter.isCursorVisible = false
                ed_search_filter.setText("")

                when (vp_list_event.currentItem) {
                    0 -> {
                        val newFragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(0)
                        newFragment?.let {
                            (newFragment as ListEventFragment).apply {
                                changeCategory.invoke(selectCategory)
                            }
                        }
                    }

                    1 -> {
                        val comingFragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(1)
                        comingFragment?.let {
                            (comingFragment as ComingEventFragment).apply {
                                changeCategory.invoke(selectCategory)
                            }
                        }
                    }

                    2 -> {
                        val expireFragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(2)
                        expireFragment?.let {
                            (expireFragment as ExpireEventFragment).apply {
                                changeCategory.invoke(selectCategory)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setUpSearchEvent() {
        ed_search_filter.setOnClickListener(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment: Fragment? = itemViewPagerAdapter.getRegisteredFragment(vp_list_event.currentItem)
        fragment?.let {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.im_header_bottom_sheet_category -> {
                when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    else -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

            }
            R.id.ed_search_filter -> {
                ed_search_filter.isCursorVisible = true
                ed_search_filter.requestFocus()
                ed_search_filter.addTextChangedListener(textWatcher)
            }
        }
    }

    private fun onChangeState() {
        when (vp_list_event.currentItem) {
            0 -> {
                val listEventFragment = itemViewPagerAdapter.getRegisteredFragment(0)
                listEventFragment?.let {
                    (listEventFragment as ListEventFragment).setOnSearchListener(ed_search_filter.text.toString())
                }
            }

            1 -> {
                val comingEventFragment = itemViewPagerAdapter.getRegisteredFragment(1)
                comingEventFragment?.let {
                    (comingEventFragment as ComingEventFragment).setOnSearchListener(ed_search_filter.text.toString())
                }
            }

            2 -> {
                val expireEventFragment = itemViewPagerAdapter.getRegisteredFragment(2)
                expireEventFragment?.let {
                    (expireEventFragment as ExpireEventFragment).setOnSearchListener(ed_search_filter.text.toString())
                }
            }
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            onChangeState()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }

    override fun getLifecycle(): LifecycleRegistry = lifeCycleRegistry

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onBackPressed() {
        if (doubleTwiceBackPress) {
            super.onBackPressed()
            FirebaseAuth.getInstance().signOut()
            googleApiClient?.disconnect()
            supportFinishAfterTransition()
            return
        }

        doubleTwiceBackPress = true
        Toast.makeText(applicationContext, "Press Back Once Again", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            doubleTwiceBackPress = false
        }, 2000)

    }
}
