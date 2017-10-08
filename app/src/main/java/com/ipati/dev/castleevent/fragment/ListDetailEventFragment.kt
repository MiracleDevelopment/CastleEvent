package com.ipati.dev.castleevent.fragment

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log

import android.view.*
import android.widget.Toast
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.DraweeTransition
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.Data
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ipati.dev.castleevent.BuildConfig
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.matrixHeightPx
import com.ipati.dev.castleevent.extension.matrixWidthPx
import com.ipati.dev.castleevent.model.Glide.loadGoogleMapStatic
import com.ipati.dev.castleevent.model.Glide.loadLogo
import com.ipati.dev.castleevent.model.Glide.loadPhotoAdvertise
import com.ipati.dev.castleevent.model.Glide.loadPhotoDetail
import com.ipati.dev.castleevent.model.GoogleCalendar.*
import com.ipati.dev.castleevent.model.GoogleCalendar.CalendarFragment.CalendarManager
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.OnUpdateInfomation
import com.ipati.dev.castleevent.model.gmsLocation.GooglePlayServiceMapManager
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent
import com.ipati.dev.castleevent.model.userManage.username
import com.ipati.dev.castleevent.service.AuthenticationStatus
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseDetailManager
import com.ipati.dev.castleevent.service.RecordedEvent.RecordListEvent
import com.ipati.dev.castleevent.utill.DialogManager
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import kotlinx.android.synthetic.main.activity_detail_event_fragment.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_get_tickets_submit.*
import java.text.SimpleDateFormat
import java.util.*


class ListDetailEventFragment : BaseFragment(), LoadingDetailData, OnUpdateInfomation, View.OnClickListener {
    private var REQUEST_ACCOUNT: Int = 1112
    private var REQUEST_GOOGLE_PLAY: Int = 1121
    private var REQUEST_PERMISSION_ACCOUNT: Int = 1111
    private var REQUEST_CALENDAR_PERMISSION: Int = 1101
    private var REQUEST_ACCOUNT_RECORD: Int = 1000

    private lateinit var realTimeDatabaseDetailManager: RealTimeDatabaseDetailManager
    private lateinit var mGoogleSharePreference: SharePreferenceGoogleSignInManager
    private lateinit var googlePlayServiceMap: GooglePlayServiceMapManager
    private lateinit var mGoogleCredentialAccount: GoogleAccountCredential
    private lateinit var mGoogleApiAvailability: GoogleApiAvailability
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var mCalendarManager: CalendarManager
    private lateinit var mRecorderEvent: RecordListEvent
    private lateinit var mAuthenticationStatus: AuthenticationStatus
    private lateinit var mDialogManager: DialogManager

    private var mCalendarTimeStamp = java.util.Calendar.getInstance()
    private var Ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var eventId: Long? = null
    private var accountBank: String? = null
    private var statusCodeGoogleApi: Int? = null
    private var mPushEvent: MakePushEvent? = null
    private var mItemListEvent: ItemListEvent? = null
    private var mCheckRest: DatabaseReference? = null
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        postponeEnterTransition()
        startPostponedEnterTransition()

        activity.window.sharedElementEnterTransition = DraweeTransition
                .createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP)
                .setDuration(200)
                .addTarget(R.id.im_detail_cover)
                .excludeChildren(android.R.id.statusBarBackground, true)

        activity.window.sharedElementReturnTransition = DraweeTransition
                .createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP)
                .setDuration(400)
                .addTarget(R.id.im_detail_cover)
                .excludeChildren(android.R.id.statusBarBackground, true)

        activity.window.exitTransition = DraweeTransition
                .createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP)
                .setDuration(400)
                .addTarget(R.id.im_detail_cover)
                .excludeChildren(android.R.id.statusBarBackground, true)

        mAuthenticationStatus = AuthenticationStatus()
        mGoogleApiAvailability = GoogleApiAvailability.getInstance()
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
        bundle = arguments
        bundle?.let {
            ViewCompat.setTransitionName(im_detail_cover, bundle?.getString(transition))
            im_detail_cover.layoutParams.width = context.matrixWidthPx(resources.displayMetrics.widthPixels)
            im_detail_cover.layoutParams.height = context.matrixHeightPx(app_bar_detail_event.layoutParams.height)

            eventId = bundle!!.getLong(listEventObject)
            realTimeDatabaseDetailManager = RealTimeDatabaseDetailManager(context, lifecycle, eventId!!, this)
        }

        bt_get_tickets.setOnClickListener { btView -> onClick(btView) }
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

        mGoogleCredentialAccount.selectedAccountName = mGoogleSharePreference.defaultSharePreferenceManager()
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
        tv_bottom_sheet_limit_access.text = "สามารถจองได้ถึงภายในวันที่ $limitTime"
    }

    private fun setUIClickable() {
        tv_receive_tickets.setOnClickListener {
            when (tv_receive_tickets.text) {
                resources.getText(R.string.expiredTickets) -> {
                    Toast.makeText(context, tv_receive_tickets.text, Toast.LENGTH_SHORT).show()
                }

                resources.getText(R.string.pleaseLogin) -> {
                    Toast.makeText(context, tv_receive_tickets.text, Toast.LENGTH_SHORT).show()
                }

                resources.getText(R.string.lessAfterDate) -> {
                    Toast.makeText(context,tv_receive_tickets.text,Toast.LENGTH_SHORT).show()
                }

                else -> {
                    mCalendarTimeStamp.timeZone = TimeZone.getDefault()
                    val msg = "คุณต้องการจองบัตร จำนวน " + number_picker.value.toString() + " ใบ" + "\n" + " ใช่ / ไม่"
                    mDialogManager.onShowConfirmDialog(msg)
                }
            }
        }
    }

    private fun setReceiveTicketEnable(stringButton: String) {
        tv_receive_tickets.setBackgroundResource(R.drawable.custom_background_receive_tickets)
        tv_receive_tickets.isFocusable = true
        tv_receive_tickets.isClickable = true
        tv_receive_tickets.text = stringButton
    }

    private fun setReceiveTicketsDisable(stringButton: String) {
        tv_receive_tickets.setBackgroundResource(R.drawable.custom_background_close_event_ripple)
        tv_receive_tickets.isClickable = true
        tv_receive_tickets.isFocusable = true
        tv_receive_tickets.text = stringButton
    }

    //Todo: Calling First When Clickable
    override fun onLoadingUpdateData(itemListEvent: ItemListEvent) {
        setOnDetailEvent(itemListEvent)
        initialToolbar(itemListEvent = itemListEvent)
        initialGoogleCredentialAccount()
        initialBottomSheet()
    }

    //Todo: Direction Update
    override fun setDataChange(mItemListEvent: ItemListEvent) {
        loadPhotoDetail(context, mItemListEvent.eventCover, im_detail_cover)
        loadPhotoAdvertise(context, mItemListEvent.eventAdvertise, im_advertise_detail)
        loadLogo(context, mItemListEvent.eventLogoCredit, im_logo_credit_detail)
        loadGoogleMapStatic(context, mItemListEvent.eventLatitude, mItemListEvent.eventLongitude, im_static_map)

        tv_detail_time.text = mItemListEvent.eventTime
        tv_detail_location.text = mItemListEvent.eventLocation
        tv_detail_description.text = mItemListEvent.eventDescription
        tv_detail_number_phone_contact.text = "092-270-7454"
        tv_detail_mail_description_contact.text = "admin@contact.co.th"
        tv_Start_price.text = "STARTING FROM ฿" + mItemListEvent.eventPrice


        mAccountBank = mItemListEvent.accountBank
        keyEvent = mItemListEvent.eventKey
        idEvent = mItemListEvent.eventId.toString()
        nameEvent = mItemListEvent.eventName
        logoEvent = mItemListEvent.eventCover
        startEvent = mItemListEvent.eventCalendarStart
        endEvent = mItemListEvent.eventCalendarEnd
        maxEvent = mItemListEvent.eventMax
        restEvent = mItemListEvent.eventRest
        startCalendar = mItemListEvent.eventCalendarStart
        endCalendar = mItemListEvent.eventCalendarEnd
        descriptionEvent = mItemListEvent.eventDescription
        priceEvent = mItemListEvent.eventPrice
        locationEvent = mItemListEvent.eventLocation

        checkedDateEvent(mItemListEvent)
    }

    private fun setOnDetailEvent(itemListEvent: ItemListEvent) {
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

        mRecorderEvent = RecordListEvent()
        mCheckRest = Ref.child("eventItem").child("eventContinue").child(keyEvent)

        checkedDateEvent(itemListEvent)
    }

    private fun checkedDateEvent(mItemListEvent: ItemListEvent) {
        if (mAuthenticationStatus.getCurrentUser() != null) {
            if (Date().before(mCalendarManager.formatDateTimeStartEvent(startCalendar!!))) {
                if (restEvent!! > 0) {
                    setReceiveTicketEnable("${mItemListEvent.eventPrice}/Tickets")
                } else {
                    setReceiveTicketsDisable(resources.getString(R.string.expiredTickets))
                }
            } else if (Date().after(mCalendarManager.formatDateTimeStartEvent(endCalendar!!))) {
                setReceiveTicketsDisable(resources.getString(R.string.lessAfterDate))
            }
        } else {
            setReceiveTicketsDisable(resources.getString(R.string.pleaseLogin))
        }
    }

    private fun setRestJoinEvent(count: Int): Int {
        return (restEvent!! - count).toInt()
    }

    private fun onCheckStatusCredentialGoogleCalendar() {
        if (onCheckGoogleApiService()) {
            onSetUserAccount()
        } else {
            statusCodeGoogleApi = mGoogleApiAvailability.isGooglePlayServicesAvailable(context)
            if (mGoogleApiAvailability.isUserResolvableError(statusCodeGoogleApi!!)) {
                showDialogErrorGoogleCalendarService(statusCodeGoogleApi!!)
            }
        }
    }


    private fun onSetUserAccount() {
        if (mGoogleSharePreference.defaultSharePreferenceManager() != null) {
            attendee = mGoogleSharePreference.defaultSharePreferenceManager()
            mGoogleCredentialAccount.selectedAccountName = mGoogleSharePreference.defaultSharePreferenceManager()
        } else {
            startActivityForResult(mGoogleCredentialAccount.newChooseAccountIntent(), REQUEST_ACCOUNT)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                addEvent()
            } else {
                requestPermissionGetAccounts()
            }
        } else {
            addEvent()
        }
    }

    private fun addEvent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                verifyGoogleAccounts()
            } else {
                verifyGoogleAccounts()
            }
        } else {
            verifyGoogleAccounts()
        }
    }

    private fun verifyGoogleAccounts() {
        if (mGoogleCredentialAccount.selectedAccountName == null) {
            if (mGoogleSharePreference.defaultSharePreferenceManager() != null) {
                mGoogleCredentialAccount.selectedAccountName = mGoogleSharePreference.defaultSharePreferenceManager()
                attendee = mGoogleSharePreference.defaultSharePreferenceManager()
                recordMyTickets()
            } else {
                startActivityForResult(mGoogleCredentialAccount.newChooseAccountIntent(), REQUEST_ACCOUNT_RECORD)
            }
        } else {
            attendee = mGoogleCredentialAccount.selectedAccountName
            recordMyTickets()
        }
    }

    private fun recordMyTickets() {
        mDialogManager.onShowLoadingDialog("ระบบกำลังดำเนินงาน")

        val day = mCalendarTimeStamp.get(java.util.Calendar.DATE)
        val month = mCalendarTimeStamp.getDisplayName(java.util.Calendar.MONTH, java.util.Calendar.LONG, Locale("th"))
        val year = mCalendarTimeStamp.get(java.util.Calendar.YEAR)

        val dateStamp = day.toString() + "-" + month.toString() + "-" + year.toString()
        val timeStamp = System.currentTimeMillis()

        mCheckRest?.runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                if (p0 != null) {
                    mDialogManager.onDismissLoadingDialog()
//                    mDialogManager.onDismissConfirmDialog()
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

                if (setRestJoinEvent(number_picker.value) >= 0) {

                    mItemListEvent?.apply {
                        accountBank = mAccountBank.toString()
                        eventKey = keyEvent.toString()
                        eventMax = maxEvent!!
                        eventRest = setRestJoinEvent(number_picker.value).toLong()
                    }

                    mRecorderEvent.pushEventRealTime(username.toString(), eventId.toString(), nameEvent.toString(), locationEvent.toString(), logoEvent!!, number_picker.value.toLong(), dateStamp, timeStamp)?.apply {
                        addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                                    MakePushEvent(mGoogleCredentialAccount).execute()
                                } else {
                                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                                }

                                mDialogManager.onDismissLoadingDialog()
                                Toast.makeText(context, "คุณทำการจองบัตรเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
                            }
                        }

                        addOnFailureListener { exception ->
                            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    mDialogManager.onDismissConfirmDialog()
                } else {

                    activity.runOnUiThread {
                        mDialogManager.onDismissConfirmDialog()
                        mDialogManager.onDismissLoadingDialog()
                        Toast.makeText(context, "ขออภัยจำนวนบัตรเหลือ $restEvent ใบ", Toast.LENGTH_SHORT).show()
                    }
                }

                p0.value = mItemListEvent
                return Transaction.success(p0)
            }
        })
    }

    //Todo : DialogConfirmFragment
    fun onNegativeConfirmFragment() {
        mDialogManager.onDismissConfirmDialog()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
            }
        }
        return false
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_get_tickets -> {
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
    }

    private fun requestPermissionGetAccounts() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
            requestPermissions(arrayOf(Manifest.permission.GET_ACCOUNTS), REQUEST_PERMISSION_ACCOUNT)
        } else {
            requestPermissions(arrayOf(Manifest.permission.GET_ACCOUNTS), REQUEST_PERMISSION_ACCOUNT)
        }
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
                        attendee = accountBank
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

            REQUEST_ACCOUNT_RECORD -> {
                data?.let {
                    accountBank = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    accountBank?.let {
                        mGoogleSharePreference.sharePreferenceManager(accountBank)
                        mGoogleCredentialAccount.selectedAccountName = accountBank
                        attendee = accountBank
                        onPositiveConfirmFragment()
                    }
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_ACCOUNT -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onCheckStatusCredentialGoogleCalendar()
                    mDialogManager.onDismissConfirmDialog()
                    recordMyTickets()
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                        recordMyTickets()
                    }
                    mDialogManager.onDismissConfirmDialog()
                }
            }
        }
    }

    fun setOnPositiveListener() {
        val intentSettingPermission = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                , Uri.parse("package:${BuildConfig.APPLICATION_ID}"))
        startActivity(intentSettingPermission)
        mDialogManager.onDismissConfirmDialog()
    }

    fun setOnNegativeListener() {
        mDialogManager.onDismissConfirmDialog()
    }


    companion object {
        private var listEventObject: String = "ListDetailEventFragment"
        private var widthObject: String = "width"
        private var heightObject: String = "height"
        private var transition: String = "transition"
        fun newInstance(width: Int, height: Int, transitionName: String, nameObject: Long): ListDetailEventFragment {
            val listDetailEventFragment = ListDetailEventFragment()
            val bundle = Bundle()
            bundle.putLong(listEventObject, nameObject)
            bundle.putInt(widthObject, width)
            bundle.putInt(heightObject, height)
            bundle.putString(transition, transitionName)
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

