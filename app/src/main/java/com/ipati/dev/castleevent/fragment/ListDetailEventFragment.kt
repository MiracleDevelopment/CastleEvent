package com.ipati.dev.castleevent.fragment

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log

import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Glide.loadLogo
import com.ipati.dev.castleevent.model.Glide.loadPhotoAdvertise
import com.ipati.dev.castleevent.model.Glide.loadPhotoDetail
import com.ipati.dev.castleevent.model.GoogleCalendar.*
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.gmsLocation.GooglePlayServiceMapManager
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseDetailManager
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import kotlinx.android.synthetic.main.activity_detail_event_fragment.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_get_tickets_submit.*
import java.util.*


class ListDetailEventFragment : Fragment(), LifecycleRegistryOwner, LoadingDetailData, OnMapReadyCallback {
    private var REQUEST_ACCOUNT: Int = 1112
    private var REQUEST_GOOGLE_PLAY: Int = 1121
    private var REQUEST_PERMISSION_ACCOUNT: Int = 1111
    private var REQUEST_CALENDAR_PERMISSION: Int = 1101

    private var mRegistry: LifecycleRegistry = LifecycleRegistry(this)

    private lateinit var realTimeDatabaseDetailManager: RealTimeDatabaseDetailManager
    private lateinit var mGoogleSharePreference: SharePreferenceGoogleSignInManager
    private lateinit var googlePlayServiceMap: GooglePlayServiceMapManager
    private lateinit var mGoogleCredentialAccount: GoogleAccountCredential
    private lateinit var mGoogleApiAvailability: GoogleApiAvailability
    private lateinit var mapFragment: MapFragment
    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var mAnimationBottomSheet: Animation
    private lateinit var bundle: Bundle
    private lateinit var mViewBottomSheetBehavior: View


    private var eventId: Long? = null
    private var accountName: String? = null
    private var userAccountName: String? = null
    private var statusCodeGoogleApi: Int? = null
    private var mPushEvent: MakePushEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        bundle = arguments
        eventId = bundle.getLong(listEventObject)

        mGoogleApiAvailability = GoogleApiAvailability.getInstance()
        realTimeDatabaseDetailManager = RealTimeDatabaseDetailManager(context, lifecycle, eventId!!, this)
        googlePlayServiceMap = GooglePlayServiceMapManager(activity, lifecycle)
        mGoogleSharePreference = SharePreferenceGoogleSignInManager(context)

        initialGoogleMapFragment()
        initialGoogleCredentialAccount()
        initialBottomSheet()
    }

    @SuppressLint("InflateParams", "ResourceType")
    private fun initialBottomSheet() {
        mViewBottomSheetBehavior = layoutInflater.inflate(R.layout.layout_get_tickets_submit, null)
        mBottomSheetDialog = BottomSheetDialog(context)
        mBottomSheetDialog.setContentView(mViewBottomSheetBehavior)

        mBottomSheetBehavior = BottomSheetBehavior.from(mViewBottomSheetBehavior.parent as View)

        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Toast.makeText(context, "Slide", Toast.LENGTH_SHORT).show()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Toast.makeText(context, "Dragging", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, "Collapsed", Toast.LENGTH_SHORT).show()
                        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_detail_event_fragment, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_get_tickets.setOnClickListener {

        }
    }

    private fun initialToolbar(itemListEvent: ItemListEvent) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar_detail_event_fragment)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_detail_event_fragment.title = itemListEvent.eventName
    }

    private fun initialDetailEvent(itemListEvent: ItemListEvent) {
        loadPhotoDetail(context, itemListEvent.eventCover, im_detail_cover)
        loadPhotoAdvertise(context, itemListEvent.eventAdvertise, im_advertise_detail)
        loadLogo(context, itemListEvent.eventLogoCredit, im_logo_credit_detail)

        tv_detail_time.text = itemListEvent.eventTime
        tv_detail_location.text = itemListEvent.eventLocation
        tv_detail_description.text = itemListEvent.eventDescription
        tv_detail_number_phone_contact.text = "092-270-7454"
        tv_detail_mail_description_contact.text = "admin@contact.co.th"

        nameEvent = itemListEvent.eventName
        startEvent = itemListEvent.eventCalendarStart
        endEvent = itemListEvent.eventCalendarEnd
        descriptionEvent = itemListEvent.eventDescription
        locationEvent = itemListEvent.eventLocation
        attendee = defaultAccountGoogleCalendar()
    }

    private fun initialGoogleCredentialAccount() {
        mGoogleCredentialAccount = GoogleAccountCredential
                .usingOAuth2(activity, Arrays.asList(CalendarScopes.CALENDAR))
                .setBackOff(ExponentialBackOff())

        mGoogleCredentialAccount.selectedAccountName = defaultAccountGoogleCalendar()
    }

    private fun defaultAccountGoogleCalendar(): String? {
        return mGoogleSharePreference.defaultSharePreferenceManager()
    }

    private fun initialGoogleMapFragment() {
        mapFragment = MapFragment.newInstance()

        activity.fragmentManager.beginTransaction()
                .replace(R.id.frame_google_map_api, mapFragment)
                .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onLoadingUpdateData(itemListEvent: ItemListEvent) {
        initialDetailEvent(itemListEvent)
        initialToolbar(itemListEvent = itemListEvent)
    }


    private fun onCheckStatusCredentialGoogleCalendar() {
        if (!onCheckGoogleApiService()) {
            statusCodeGoogleApi = mGoogleApiAvailability.isGooglePlayServicesAvailable(context)
            if (mGoogleApiAvailability.isUserResolvableError(statusCodeGoogleApi!!)) {
                ShowDialogErrorGoogleSCalendarService(statusCodeGoogleApi!!)
            }
        } else if (mGoogleCredentialAccount.selectedAccountName == null) {
            permissionGoogleAccount()
        } else {
            Toast.makeText(context, "addEvent", Toast.LENGTH_LONG).show()
            MakePushEvent(mGoogleCredentialAccount).execute()
        }
    }

    private fun permissionGoogleAccount() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(Array(0, { android.Manifest.permission.GET_ACCOUNTS }), REQUEST_PERMISSION_ACCOUNT)
            } else {
                onSetUserAccount()
            }
        } else {
            onSetUserAccount()
        }
    }

    private fun onSetUserAccount() {
        userAccountName = mGoogleSharePreference.defaultSharePreferenceManager()
        if (userAccountName != null) {
            mGoogleCredentialAccount.selectedAccountName = userAccountName
            onCheckStatusCredentialGoogleCalendar()
        } else {
            if (mGoogleCredentialAccount.selectedAccountName == null) {
                startActivityForResult(mGoogleCredentialAccount.newChooseAccountIntent(), REQUEST_ACCOUNT)
            }
        }
    }


    private fun ShowDialogErrorGoogleSCalendarService(connectionCode: Int): Dialog {
        val dialog: Dialog = mGoogleApiAvailability.getErrorDialog(activity, connectionCode, REQUEST_GOOGLE_PLAY)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }

    private fun onCheckGoogleApiService(): Boolean {
        mGoogleApiAvailability = GoogleApiAvailability.getInstance()
        statusCodeGoogleApi = mGoogleApiAvailability.isGooglePlayServicesAvailable(activity)
        return statusCodeGoogleApi == ConnectionResult.SUCCESS
    }

    //Todo: Method Override
    override fun onMapReady(p0: GoogleMap?) {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun getLifecycle(): LifecycleRegistry {
        return mRegistry
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ACCOUNT -> {
                data?.let {
                    accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    accountName.let {
                        mGoogleSharePreference.sharePreferenceManager(accountName)
                        mGoogleCredentialAccount.selectedAccountName = accountName
                        onCheckStatusCredentialGoogleCalendar()
                    }
                }
            }

            REQUEST_GOOGLE_PLAY -> {
                Toast.makeText(context, "Please install googlePlayService ", Toast.LENGTH_LONG).show()
            }

            REQUEST_CALENDAR_PERMISSION -> {
                mPushEvent = MakePushEvent(mGoogleCredentialAccount)
                mPushEvent?.execute()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_ACCOUNT -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onCheckStatusCredentialGoogleCalendar()
                }
            }
        }
    }

    companion object {
        var listEventObject: String = "ListDetailEventFragment"
        fun newInstance(nameObject: Long): ListDetailEventFragment {
            val listDetailEventFragment = ListDetailEventFragment()
            val bundle = Bundle()
            bundle.putLong(listEventObject, nameObject)
            listDetailEventFragment.arguments = bundle
            return listDetailEventFragment
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class MakePushEvent(mGoogleCredential: GoogleAccountCredential) : AsyncTask<Void, Void, Event?>() {
        private var credential: GoogleAccountCredential = mGoogleCredential
        private var mListError: Exception? = null
        private var mService: Calendar? = null

        private var mGoogleCalendarInsertEvent: GoogleCalendarInsertEvent = GoogleCalendarInsertEvent(
                nameEvent, locationEvent, descriptionEvent)

        private var transport: HttpTransport = AndroidHttp.newCompatibleTransport()
        private var jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

        init {
            mService = Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Castle EventApp")
                    .build()
        }

        override fun doInBackground(vararg p0: Void?): Event? {
            return try {
                mService?.events()?.insert("primary", mGoogleCalendarInsertEvent.requestEvent())?.execute()
            } catch (e: Exception) {
                cancel(true)
                mListError = e
                null
            }
        }


        override fun onPostExecute(result: Event?) {
            super.onPostExecute(result)
            Log.d("resultTaskInsertEvent", result?.htmlLink)

        }

        override fun onCancelled() {
            super.onCancelled()
            if (mListError != null) {
                when (mListError) {
                    is UserRecoverableAuthIOException -> {
                        startActivityForResult((mListError as UserRecoverableAuthIOException).intent, REQUEST_CALENDAR_PERMISSION)
                    }
                    else -> {
                        Log.d("errorPushEvent", mListError?.message.toString())
                    }
                }
            }
        }
    }

}

