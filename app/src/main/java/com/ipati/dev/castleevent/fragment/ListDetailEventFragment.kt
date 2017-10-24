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
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.firebase.database.*
import com.ipati.dev.castleevent.BuildConfig
import com.ipati.dev.castleevent.base.BaseFragment
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.matrixHeightPx
import com.ipati.dev.castleevent.extension.matrixWidthPx
import com.ipati.dev.castleevent.model.Fresco.loadGoogleMapStatic
import com.ipati.dev.castleevent.model.Fresco.loadLogo
import com.ipati.dev.castleevent.model.Fresco.loadPhotoAdvertise
import com.ipati.dev.castleevent.model.Fresco.loadPhotoDetail
import com.ipati.dev.castleevent.model.GoogleCalendar.*
import com.ipati.dev.castleevent.model.GoogleCalendar.CalendarFragment.CalendarManager
import com.ipati.dev.castleevent.model.Date.DateManager
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.OnUpdateInformation
import com.ipati.dev.castleevent.model.GmsLocation.GooglePlayServiceMapManager
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import com.ipati.dev.castleevent.model.UserManager.username
import com.ipati.dev.castleevent.service.AuthenticationStatus
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseDetailManager
import com.ipati.dev.castleevent.service.RecordedEvent.RecordListEvent
import com.ipati.dev.castleevent.utill.DialogManager
import com.ipati.dev.castleevent.utill.SharePreferenceGoogleSignInManager
import kotlinx.android.synthetic.main.activity_detail_event_fragment.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_get_tickets_submit.*
import java.util.*


class ListDetailEventFragment : BaseFragment(), LoadingDetailData, OnUpdateInformation, View.OnClickListener {
    private var REQUEST_ACCOUNT: Int = 1112
    private var REQUEST_GOOGLE_PLAY: Int = 1121
    private var REQUEST_PERMISSION_ACCOUNT: Int = 1111
    private var REQUEST_CALENDAR_PERMISSION: Int = 1101
    private var REQUEST_ACCOUNT_RECORD: Int = 1000

    private lateinit var realTimeDatabaseDetailManager: RealTimeDatabaseDetailManager
    private lateinit var googleSharePreference: SharePreferenceGoogleSignInManager
    private lateinit var googlePlayServiceMap: GooglePlayServiceMapManager
    private lateinit var googleCredentialAccount: GoogleAccountCredential
    private lateinit var googleApiAvailability: GoogleApiAvailability
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var calendarManager: CalendarManager
    private lateinit var authenticationStatus: AuthenticationStatus
    private lateinit var dialogManager: DialogManager
    private lateinit var dateManager: DateManager

    private var ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var eventId: Long? = null
    private var accountBank: String? = null
    private var statusCodeGoogleApi: Int? = null
    private var pushEvent: MakePushEvent? = null
    private var restItemEvent: ItemListEvent? = null
    private var recorderEvent: RecordListEvent? = null
    private var checkRest: DatabaseReference? = null
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
                .setDuration(200)
                .addTarget(R.id.im_detail_cover)
                .excludeChildren(android.R.id.statusBarBackground, true)

        activity.window.exitTransition = DraweeTransition
                .createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP)
                .setDuration(200)
                .addTarget(R.id.im_detail_cover)
                .excludeChildren(android.R.id.statusBarBackground, true)

        authenticationStatus = AuthenticationStatus()
        googleApiAvailability = GoogleApiAvailability.getInstance()
        googlePlayServiceMap = GooglePlayServiceMapManager(activity, lifecycle)
        googleSharePreference = SharePreferenceGoogleSignInManager(context)
        calendarManager = CalendarManager(context)
        dialogManager = DialogManager(activity)
        dateManager = DateManager(context)
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
        googleCredentialAccount = GoogleAccountCredential
                .usingOAuth2(activity, Arrays.asList(CalendarScopes.CALENDAR))
                .setBackOff(ExponentialBackOff())

        googleCredentialAccount.selectedAccountName = googleSharePreference.defaultSharePreferenceManager()
    }


    @SuppressLint("InflateParams", "ResourceType")
    private fun initialBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(view?.findViewById(R.id.bottom_sheet))
        bottomSheetBehavior.peekHeight = li_header_bottom_sheet.height
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (bottomSheetBehavior.state) {
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
            when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_up)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                BottomSheetBehavior.STATE_COLLAPSED -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_down)
                }
            }
        }

        setGetTicketsClickable()
    }

    private fun setGetTicketsClickable() {
        tv_receive_tickets.setOnClickListener {
            when (tv_receive_tickets.text) {
                resources.getText(R.string.expiredTickets) -> {
                    dialogManager.onShowMissingDialog(resources.getString(R.string.expiredTickets))
                }

                resources.getText(R.string.pleaseLogin) -> {
                    dialogManager.onShowMissingDialog(resources.getString(R.string.pleaseLogin))
                }

                resources.getText(R.string.lessAfterDate) -> {
                    dialogManager.onShowMissingDialog(resources.getString(R.string.lessAfterDate))
                }

                resources.getString(R.string.closeEvent) -> {
                    dialogManager.onShowMissingDialog(resources.getString(R.string.closeEvent))
                }

                else -> {
                    val msg = "คุณต้องการจองบัตร จำนวน " + number_picker.value.toString() + " ใบ" + "\n" + " ใช่ / ไม่"
                    dialogManager.onShowConfirmDialog(msg)
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
    @SuppressLint("SetTextI18n")
    override fun setDataChange(itemListEvent: ItemListEvent) {
        loadPhotoDetail(context, itemListEvent.eventCover, im_detail_cover)
        loadPhotoAdvertise(context, itemListEvent.eventAdvertise, im_advertise_detail)
        loadLogo(context, itemListEvent.eventLogoCredit, im_logo_owner_event)
        loadGoogleMapStatic(context, itemListEvent.eventLatitude, itemListEvent.eventLongitude, im_static_map)

        tv_detail_time.text = itemListEvent.eventTime
        tv_detail_location.text = itemListEvent.eventLocation
        tv_detail_description.text = itemListEvent.eventDescription
        tv_detail_number_phone_contact.text = itemListEvent.eventPhone
        tv_detail_mail_description_contact.text = itemListEvent.eventEmail
        tv_Start_price.text = "STARTING FROM ฿${itemListEvent.eventPrice}"

        //Todo: Bottom Sheet Layout
        tv_bottom_sheet_header_event.text = nameEvent
        tv_bottom_sheet_description_event.text = descriptionEvent
        tv_bottom_sheet_limit_access.text = "สามารถจองได้ถึงภายในวันที่ ${dateManager.convertStringDate(itemListEvent.eventCalendarStart)}"


        bankAccount = itemListEvent.accountBank
        keyEvent = itemListEvent.eventKey
        idEvent = itemListEvent.eventId.toString()
        nameEvent = itemListEvent.eventName
        categoryNameEvent = itemListEvent.categoryName
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

        dateManager.getStatusTickets(itemListEvent, { status: String ->
            statusButton(status)
        })

    }

    //Todo: Call from intent
    @SuppressLint("SetTextI18n")
    private fun setOnDetailEvent(itemListEvent: ItemListEvent) {
        loadPhotoDetail(context, itemListEvent.eventCover, im_detail_cover)
        loadPhotoAdvertise(context, itemListEvent.eventAdvertise, im_advertise_detail)
        loadLogo(context, itemListEvent.eventLogoCredit, im_logo_owner_event)
        loadGoogleMapStatic(context, itemListEvent.eventLatitude, itemListEvent.eventLongitude, im_static_map)

        tv_detail_time.text = itemListEvent.eventTime
        tv_detail_location.text = itemListEvent.eventLocation
        tv_detail_description.text = itemListEvent.eventDescription
        tv_detail_number_phone_contact.text = itemListEvent.eventPhone
        tv_detail_mail_description_contact.text = itemListEvent.eventEmail
        tv_Start_price.text = "STARTING FROM ฿${itemListEvent.eventPrice}"

        //Todo: Bottom Sheet Layout
        tv_bottom_sheet_header_event.text = itemListEvent.eventName
        tv_bottom_sheet_description_event.text = itemListEvent.eventDescription
        tv_bottom_sheet_limit_access.text = "สามารถจองได้ถึงภายในวันที่ ${dateManager.convertStringDate(itemListEvent.eventCalendarStart)}"

        bankAccount = itemListEvent.accountBank
        keyEvent = itemListEvent.eventKey
        idEvent = itemListEvent.eventId.toString()
        nameEvent = itemListEvent.eventName
        categoryNameEvent = itemListEvent.categoryName
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


        dateManager.getStatusTickets(itemListEvent, { status: String ->
            statusButton(status)
        })

    }

    private fun statusButton(status: String) {
        when (status) {
            resources.getString(R.string.expiredTickets) -> {
                setReceiveTicketsDisable(status)
            }
            resources.getString(R.string.lessAfterDate) -> {
                setReceiveTicketsDisable(status)
            }

            resources.getString(R.string.pleaseLogin) -> {
                setReceiveTicketsDisable(status)
            }

            resources.getString(R.string.closeEvent) -> {
                setReceiveTicketsDisable(status)
            }
            else -> {
                setReceiveTicketEnable(status)
            }
        }
    }

    private fun setRestJoinEvent(count: Int): Int {
        return (restEvent!! - count).toInt()
    }

    private fun onCheckStatusCredentialGoogleCalendar() {
        if (onCheckGoogleApiService()) {
            onSetUserAccount()
        } else {
            statusCodeGoogleApi = googleApiAvailability.isGooglePlayServicesAvailable(context)
            if (googleApiAvailability.isUserResolvableError(statusCodeGoogleApi!!)) {
                showDialogErrorGoogleCalendarService(statusCodeGoogleApi!!)
            }
        }
    }


    private fun onSetUserAccount() {
        if (googleSharePreference.defaultSharePreferenceManager() != null) {
            attendee = googleSharePreference.defaultSharePreferenceManager()
            googleCredentialAccount.selectedAccountName = googleSharePreference.defaultSharePreferenceManager()
        } else {
            startActivityForResult(googleCredentialAccount.newChooseAccountIntent(), REQUEST_ACCOUNT)
        }
    }

    private fun showDialogErrorGoogleCalendarService(connectionCode: Int): Dialog {
        val dialog: Dialog = googleApiAvailability.getErrorDialog(activity, connectionCode, REQUEST_GOOGLE_PLAY)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }

    private fun onCheckGoogleApiService(): Boolean {
        googleApiAvailability = GoogleApiAvailability.getInstance()
        statusCodeGoogleApi = googleApiAvailability.isGooglePlayServicesAvailable(activity)
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

    fun onMissingDialogConfirmFragment() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        dialogManager.onDismissMissingDialog()
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
        if (googleCredentialAccount.selectedAccountName == null) {
            if (googleSharePreference.defaultSharePreferenceManager() != null) {
                googleCredentialAccount.selectedAccountName = googleSharePreference.defaultSharePreferenceManager()
                attendee = googleSharePreference.defaultSharePreferenceManager()
                recordMyTickets()
            } else {
                startActivityForResult(googleCredentialAccount.newChooseAccountIntent(), REQUEST_ACCOUNT_RECORD)
            }
        } else {
            attendee = googleCredentialAccount.selectedAccountName
            recordMyTickets()
        }
    }

    private fun recordMyTickets() {
        checkRest = ref.child("eventItem").child("eventContinue").child(keyEvent)
        checkRest?.runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                if (p0 != null) {
                    dialogManager.onDismissLoadingDialog()
                    dialogManager.onDismissConfirmDialog()

                    Log.d("TransactionStatus", p0.message.toString())
                } else {
                    recorderEvent = RecordListEvent()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                    dialogManager.onDismissConfirmDialog()
                    dialogManager.onDismissLoadingDialog()

                    recorderEvent?.pushEventRealTime(username, eventId.toString(), nameEvent
                            , locationEvent, logoEvent, number_picker.value.toLong()
                            , dateManager.getCurrentDate(), java.util.Calendar.getInstance().timeInMillis)
                            ?.addOnCompleteListener { task ->
                                if (!task.isComplete) {
                                    dialogManager.onShowMissingDialog(task.exception?.message!!)
                                } else {
                                    MakePushEvent(googleCredentialAccount).execute()
                                    Log.d("TransactionStatus", "Success")
                                }
                            }
                }
            }

            override fun doTransaction(p0: MutableData?): Transaction.Result {
                p0?.let {
                    restItemEvent = p0.getValue(ItemListEvent::class.java)
                    restItemEvent?.let {
                        if (setRestJoinEvent(number_picker.value) >= 0) {
                            restItemEvent?.eventRest = (setRestJoinEvent(number_picker.value).toLong())
                        } else {
                            dialogManager.onShowMissingDialog("จำนวเหลือเพียง ${setRestJoinEvent(number_picker.value)}")
                        }
                    } ?: Transaction.success(p0)
                    p0.value = restItemEvent
                }
                return Transaction.success(p0)
            }
        })

        dialogManager.onShowLoadingDialog("ระบบกำลังดำเนินงาน")
    }


    //Todo : DialogConfirmFragment
    fun onNegativeConfirmFragment() {
        dialogManager.onDismissConfirmDialog()
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
                when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_down)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_up)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
                        googleSharePreference.sharePreferenceManager(accountBank)
                        googleCredentialAccount.selectedAccountName = accountBank
                        attendee = accountBank
                    }
                }
            }

            REQUEST_GOOGLE_PLAY -> {
                Toast.makeText(context, "Please install googlePlayService ", Toast.LENGTH_LONG).show()
            }

            REQUEST_CALENDAR_PERMISSION -> {
                pushEvent = MakePushEvent(googleCredentialAccount)
                pushEvent?.execute()
            }

            REQUEST_ACCOUNT_RECORD -> {
                data?.let {
                    accountBank = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    accountBank?.let {
                        googleSharePreference.sharePreferenceManager(accountBank)
                        googleCredentialAccount.selectedAccountName = accountBank
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
                    dialogManager.onDismissConfirmDialog()
                    recordMyTickets()
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                        recordMyTickets()
                    }
                    dialogManager.onDismissConfirmDialog()
                }
            }
        }
    }

    fun setOnPositiveListener() {
        val intentSettingPermission = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                , Uri.parse("package:${BuildConfig.APPLICATION_ID}"))
        startActivity(intentSettingPermission)
        dialogManager.onDismissConfirmDialog()
    }

    fun setOnNegativeListener() {
        dialogManager.onDismissConfirmDialog()
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
        private var listError: Exception? = null
        private var service: Calendar? = null

        private var googleCalendarInsertEvent: GoogleCalendarInsertEvent = GoogleCalendarInsertEvent(context,
                nameEvent, locationEvent, descriptionEvent)

        private var transport: HttpTransport = AndroidHttp.newCompatibleTransport()
        private var jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

        init {
            service = Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Castle EventApp")
                    .build()
        }


        override fun doInBackground(vararg p0: Void?): Event? {
            return try {
                service?.events()?.insert("primary", googleCalendarInsertEvent.requestEvent())?.execute()
            } catch (e: Exception) {
                cancel(true)
                listError = e
                null
            }
        }


        override fun onPostExecute(result: Event?) {
            super.onPostExecute(result)
            Log.d("resultTaskInsertEvent", result?.htmlLink)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            floating_bt_close.setImageResource(R.mipmap.ic_keyboard_arrow_up)
            dialogManager.onDismissLoadingDialog()
        }

        override fun onCancelled() {
            super.onCancelled()
            if (listError != null) {
                when (listError) {
                    is UserRecoverableAuthIOException -> {
                        startActivityForResult((listError as UserRecoverableAuthIOException).intent, REQUEST_CALENDAR_PERMISSION)
                    }
                    else -> {
                        dialogManager.onShowMissingDialog(listError?.message.toString())
                    }
                }
            }
        }
    }

}

