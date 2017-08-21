package com.ipati.dev.castleevent.fragment

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import android.view.*
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.ipati.dev.castleevent.ListEventActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Glide.loadLogo
import com.ipati.dev.castleevent.model.Glide.loadPhotoAdvertise
import com.ipati.dev.castleevent.model.Glide.loadPhotoDetail
import com.ipati.dev.castleevent.model.LoadingDetailData
import com.ipati.dev.castleevent.model.OnBackPress
import com.ipati.dev.castleevent.model.gmsLocation.GooglePlayServiceMapManager
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent
import com.ipati.dev.castleevent.service.FirebaseService.RealTimeDatabaseDetailManager
import kotlinx.android.synthetic.main.activity_detail_event_fragment.*

class ListDetailEventFragment : Fragment(), LifecycleRegistryOwner, LoadingDetailData, OnMapReadyCallback {
    var mRegistry: LifecycleRegistry = LifecycleRegistry(this)
    lateinit var googlePlayServiceMap: GooglePlayServiceMapManager
    lateinit var realTimeDatabaseDetailManager: RealTimeDatabaseDetailManager
    lateinit var bundle: Bundle
    lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        bundle = arguments
        eventId = bundle.getString(listEventObject)
        realTimeDatabaseDetailManager = RealTimeDatabaseDetailManager(context, lifecycle, eventId, this)
        googlePlayServiceMap = GooglePlayServiceMapManager(activity, lifecycle)
        googlePlayServiceMap.initialMapFragment().getMapAsync(this)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_detail_event_fragment, container, false)

    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_view_get_tickets.setOnClickListener { Toast.makeText(context, "Buy", Toast.LENGTH_SHORT).show() }
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
    }

    private fun onBackPressDetailEvent() {
        val onBackPress: OnBackPress = activity as ListEventActivity
        onBackPress.onBackPressFragment()
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressDetailEvent()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun getLifecycle(): LifecycleRegistry {
        return mRegistry
    }


    override fun onLoadingUpdateData(itemListEvent: ItemListEvent) {
        initialDetailEvent(itemListEvent)
        initialToolbar(itemListEvent = itemListEvent)
    }

    companion object {
        var listEventObject: String = "ListDetailEventFragment"
        fun newInstance(nameObject: String): ListDetailEventFragment {
            val listDetailEventFragment = ListDetailEventFragment()
            val bundle = Bundle()
            bundle.putString(listEventObject, nameObject)
            listDetailEventFragment.arguments = bundle
            return listDetailEventFragment
        }
    }
}
