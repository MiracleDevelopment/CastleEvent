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
import com.ipati.dev.castleevent.model.Glide.loadPhotoDetial
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
        eventId = bundle.getString(keyObject)
        realTimeDatabaseDetailManager = RealTimeDatabaseDetailManager(context, lifecycle, eventId, this)
        googlePlayServiceMap = GooglePlayServiceMapManager(activity, lifecycle)
        googlePlayServiceMap.initialMapFragment().getMapAsync(this)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_detail_event_fragment, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun initialToolbar(itemListEvent: ItemListEvent) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar_detail_event_fragment)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_detail_event_fragment.title = itemListEvent.eventName
    }

    fun initialDetailEvent(itemListEvent: ItemListEvent) {
        loadPhotoDetial(context, itemListEvent.eventCover, im_detail_cover)
        tv_detail_time.text = itemListEvent.eventTime
        tv_detail_location.text = itemListEvent.eventLocation
        tv_detail_description.text = itemListEvent.eventDescription
        tv_detail_number_phone_contact.text = "092-270-7454"
        tv_detail_mail_description_contact.text = "admin@contact.co.th"
    }

    fun onBackPressDetailEvent() {
        val onBackPress: OnBackPress = activity as ListEventActivity
        onBackPress.onBackPressFragment()
    }

    override fun onMapReady(p0: GoogleMap?) {
        Toast.makeText(context, "Connecting Google Map", Toast.LENGTH_SHORT).show()
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
        var keyObject: String = "ListDetailEventFragment"
        fun newInstance(nameObject: String): ListDetailEventFragment {
            val listDetailEventFragment: ListDetailEventFragment = ListDetailEventFragment()
            val bundle: Bundle = Bundle()
            bundle.putString(keyObject, nameObject)
            listDetailEventFragment.arguments = bundle
            return listDetailEventFragment
        }
    }
}
