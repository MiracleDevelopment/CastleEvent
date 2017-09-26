package com.ipati.dev.castleevent.fragment

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.Log

import android.view.*
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.GoogleMap
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
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Glide.loadGoogleMapStatic
import com.ipati.dev.castleevent.model.Glide.loadLogo
import com.ipati.dev.castleevent.model.Glide.loadPhotoAdvertise
import com.ipati.dev.castleevent.model.Glide.loadPhotoDetail
import com.ipati.dev.castleevent.model.GoogleCalendar.*
import com.ipati.dev.castleevent.model.GoogleCalendar.CalendarFragment.CalendarManager
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.gmsLocation.GooglePlayServiceMapManager
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent
import com.ipati.dev.castleevent.model.userManage.username
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseDetailManager
import com.ipati.dev.castleevent.service.RecordedEvent.RecordListEvent
import com.ipati.dev.castleevent.utill.DialogManager
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import kotlinx.android.synthetic.main.activity_detail_event_fragment.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_get_tickets_submit.*
import java.text.SimpleDateFormat
import java.util.*


class ListDetailEventFragment : BaseFragment(), LoadingDetailData {
    private var REQUEST_ACCOUNT: Int = 1112
    private var REQUEST_GOOGLE_PLAY: Int = 1121
    private var REQUEST_PERMISSION_ACCOUNT: Int = 1111
    private var REQUEST_CALENDAR_PERMISSION: Int = 1101

    private lateinit var realTimeDatabaseDetailManager: RealTimeDatabaseDetailManager
    private lateinit var mGoogleSharePreference: SharePreferenceGoogleSignInManager
    private lateinit var googlePlayServiceMap: GooglePlayServiceMapManager
    private lateinit var mGoogleCredentialAccount: GoogleAccountCredential
    private lateinit var mGoogleApiAvailability: GoogleApiAvailability
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var mCalendarManager: CalendarManager
    private lateinit var mRecorderEvent: RecordListEvent

    private lateinit var mDialogManager: DialogManager
    private lateinit var bundle: Bundle

    private var mCalendarTimeStamp = java.util.Calendar.getInstance()
    private var Ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var eventId: Long? = null
    private var accountBank: String? = null
    private var userAccountName: String? = null
    private var statusCodeGoogleApi: Int? = null
    private var mPushEvent: MakePushEvent? = null
    private var mItemListEvent: ItemListEvent? = null
    private var mCheckRest: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        bundle = arguments
        eventId = bundle.getLong(listEventObject)

        mGoogleApiAvailability = GoogleApiAvailability.getInstance()
        realTimeDatabaseDetailManager = RealTimeDatabaseDetailManager(context, lifecycle, eventId!!, this)
        googlePlayServiceMap = GooglePlayServiceMapManager(activity, lifecycle)
        mGoogleSharePreference = SharePreferenceGoogleSignInManager(context)
        mCalendarManager = CalendarManager(context)
        mDialogManager = DialogManager(activity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_detail_event_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_get_tickets.setOnClickListener {
            when (mBottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_down)
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_up)
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
    }

    private fun initialToolbar(itemListEvent: ItemListEvent) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar_detail_event_fragment)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_detail_event_fragment.title = itemListEvent.eventName
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


    @SuppressLint("InflateParams", "ResourceType")
    private fun initialBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(view?.findViewById(R.id.bottom_sheet))
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (mBottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_up)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_down)
                    }
                }
            }
        })

        floating_bt_close.setOnClickListener {
            when (mBottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_up)
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                BottomSheetBehavior.STATE_COLLAPSED -> {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_down)
                }
            }
        }

        onShowBottomSheet()
        setUIClickable()
    }

    private fun onShowBottomSheet() {
        val mDateLimitFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale("th"))
        val limitDate = mDateLimitFormat.parse(startCalendar)

        val mCalendar = java.util.Calendar.getInstance()
        mCalendar.timeZone = TimeZone.getTimeZone("UTC")
        mCalendar.timeInMillis = limitDate.time

        val limitTime = (mCalendar.get(java.util.Calendar.DATE)).toString() + " " + mCalendar.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, Locale("th")) + " " + mCalendar.get(java.util.Calendar.YEAR).toString()
        tv_bottom_sheet_header_event.text = nameEvent
        tv_bottom_sheet_description_event.text = descriptionEvent
        tv_bottom_sheet_limit_access.text = "สามารถจองได้ถึงภายในวันที่ " + limitTime



        if (restEvent!! > 0) {
            if (Date().after(mCalendarManager.formatDateTimeStartEvent(startCalendar!!))) {
                setReceiveTicketsDisable()
            } else {
                tv_receive_tickets.setBackgroundResource(R.drawable.background_get_tickets)
                tv_receive_tickets.text = priceEvent + " / " + "TICKETS"
            }
        } else {
            setReceiveTicketsDisable()
        }
    }

    private fun setUIClickable() {
        tv_receive_tickets.setOnClickListener {
            if (restEvent!! > 0) {
                if (Date().after(mCalendarManager.formatDateTimeStartEvent(startCalendar!!))) {
                    Toast.makeText(context, "ขออภัยเลยกำหนดการจองบัตรแล้วค่ะ", Toast.LENGTH_SHORT).show()
                } else {
                    mCalendarTimeStamp.timeZone = TimeZone.getDefault()
                    val msg = "คุณต้องการจองบัตร จำนวน " + number_picker.value.toString() + " ใบ" + "\n" + " ใช่ / ไม่"
                    mDialogManager.onShowConfirmDialog(msg)
                }
            } else {
                setReceiveTicketsDisable()
                Toast.makeText(context, "ขออภัยบัตรหมดแล้วค่ะ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setReceiveTicketsDisable() {
        tv_receive_tickets.setBackgroundResource(R.drawable.custom_background_close_event_ripple)
        tv_receive_tickets.isClickable = true
        tv_receive_tickets.isFocusable = true
        tv_receive_tickets.text = "บัตรหมดแล้ว"
    }

    //Todo: Calling First When Clickable
    override fun onLoadingUpdateData(itemListEvent: ItemListEvent) {
        initialDetailEvent(itemListEvent)
        initialToolbar(itemListEvent = itemListEvent)
        initialGoogleCredentialAccount()
        initialBottomSheet()
    }

    private fun initialDetailEvent(itemListEvent: ItemListEvent) {
        loadPhotoDetail(context, itemListEvent.eventCover, im_detail_cover)
        loadPhotoAdvertise(context, itemListEvent.eventAdvertise, im_advertise_detail)
        loadLogo(context, itemListEvent.eventLogoCredit, im_logo_credit_detail)
        loadGoogleMapStatic(context, itemListEvent.eventLatitude, itemListEvent.eventLongitude, im_static_map)

        tv_detail_time.text = itemListEvent.eventTime
        tv_detail_location.text = itemListEvent.eventLocation
        tv_detail_description.text = itemListEvent.eventDescription
        tv_detail_number_phone_contact.text = "092-270-7454"
        tv_detail_mail_description_contact.text = "admin@contact.co.th"
        tv_Start_price.text = "STARTING FROM ฿" + itemListEvent.eventPrice

        mAccountBank = itemListEvent.accountBank
        keyEvent = itemListEvent.eventKey
        idEvent = itemListEvent.eventId.toString()
        nameEvent = itemListEvent.eventName
        logoEvent = itemListEvent.eventCover
        startEvent = itemListEvent.eventCalendarStart
        endEvent = itemListEvent.eventCalendarEnd
        maxEvent = itemListEvent.eventMax
        restEvent = itemListEvent.eventRest
        startCalendar = itemListEvent.eventCalendarStart
        endCalendar = itemListEvent.eventCalendarEnd
        descriptionEvent = itemListEvent.eventDescription
        priceEvent = itemListEvent.eventPrice
        locationEvent = itemListEvent.eventLocation
        attendee = defaultAccountGoogleCalendar()

        mRecorderEvent = RecordListEvent()
        mCheckRest = Ref.child("eventItem").child("eventContinue").child(keyEvent)

        Log.d("timeDate", startEvent.toString() + " : " + endEvent.toString())
    }

    private fun onCheckStatusCredentialGoogleCalendar() {
        if (!onCheckGoogleApiService()) {
            statusCodeGoogleApi = mGoogleApiAvailability.isGooglePlayServicesAvailable(context)
            if (mGoogleApiAvailability.isUserResolvableError(statusCodeGoogleApi!!)) {
                showDialogErrorGoogleCalendarService(statusCodeGoogleApi!!)
            }
        } else if (mGoogleCredentialAccount.selectedAccountName == null) {
            permissionGoogleAccount()
        } else {
            Toast.makeText(context, "จองอีเว้นเรียบร้อยแล้ว", Toast.LENGTH_LONG).show()
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


    private fun showDialogErrorGoogleCalendarService(connectionCode: Int): Dialog {
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

    //Todo: DialogConfirmFragment
    fun onPositiveConfirmFragment() {
        val day = mCalendarTimeStamp.get(java.util.Calendar.DATE)
        val month = mCalendarTimeStamp.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, Locale("th"))
        val year = mCalendarTimeStamp.get(java.util.Calendar.YEAR)

        val dateStamp = day.toString() + "-" + month.toString() + "-" + year.toString()
        val timeStamp = System.currentTimeMillis()


        mCheckRest?.runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                if (p0 != null) {
                    Log.d("TransactionStatus", p0.message.toString())
                } else {
                    Log.d("TransactionStatus", "Success")
                }
            }

            override fun doTransaction(p0: MutableData?): Transaction.Result {
                mItemListEvent = p0?.getValue(ItemListEvent::class.java)!!

                if (mItemListEvent == null) {
                    return Transaction.success(p0)
                }

                if (restEvent!! - number_picker.value >= 0) {
                    mItemListEvent?.apply {
                        accountBank = mAccountBank.toString()
                        eventKey = keyEvent.toString()
                        eventMax = maxEvent!!
                        eventRest = (restEvent?.toInt()!! - number_picker.value).toLong()
                        restEvent = eventRest
                    }

                    mRecorderEvent.pushEventRealTime(username.toString(), eventId.toString(), nameEvent.toString(), locationEvent.toString(), logoEvent!!, number_picker.value.toLong(), dateStamp, timeStamp)?.addOnCompleteListener { task ->
                        if (task.isComplete) {
                            FirebaseMessaging.getInstance().subscribeToTopic("news")
                            onCheckStatusCredentialGoogleCalendar()
                        }

                    }?.addOnFailureListener { exception ->
                        Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    mDialogManager.onDismissConfirmDialog()
                } else {
                    activity.runOnUiThread {
                        Toast.makeText(context, "ขออภัยจำนวนบัตรเหลือ $restEvent ใบ", Toast.LENGTH_SHORT).show()
                    }
                }

                p0.value = mItemListEvent
                return Transaction.success(p0)
            }
        }, true)
    }

    //Todo : DialogConfirmFragment
    fun onNegativeConfirmFragment() {
        mDialogManager.onDismissConfirmDialog()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ACCOUNT -> {
                data?.let {
                    accountBank = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    accountBank.let {
                        mGoogleSharePreference.sharePreferenceManager(accountBank)
                        mGoogleCredentialAccount.selectedAccountName = accountBank
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

        override fun onPreExecute() {
            super.onPreExecute()
            mDialogManager.onShowLoadingDialog("ระบบกำลังดำเนินงาน")
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
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_up)
            mDialogManager.onDismissLoadingDialog()
        }

        override fun onCancelled() {
            super.onCancelled()
            if (mListError != null) {
                when (mListError) {
                    is UserRecoverableAuthIOException -> {
                        mDialogManager.onDismissLoadingDialog()
                        startActivityForResult((mListError as UserRecoverableAuthIOException).intent, REQUEST_CALENDAR_PERMISSION)
                    }
                    else -> {
                        mDialogManager.onDismissLoadingDialog()
                        Log.d("errorAsyncTaskPushEvent", mListError?.toString())
                    }
                }
            }
        }
    }

}

