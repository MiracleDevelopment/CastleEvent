package com.ipati.dev.castleevent.model.gmsLocation

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.MapFragment
import com.ipati.dev.castleevent.R

class GooglePlayServiceMapManager(activity: Activity, lifecycle: Lifecycle) : LifecycleObserver {
    var mLifecycle: Lifecycle? = null
    var activity: Activity? = null
    var view: Int? = null

    init {
        this.activity = activity
        this.mLifecycle = lifecycle
        this.view = view
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        initialGoogleApiClient()
    }


    fun initialGoogleApiClient(): GoogleApiClient {
        val googleApiClient: GoogleApiClient = GoogleApiClient.Builder(activity!!)
                .addOnConnectionFailedListener { connectionResult -> Toast.makeText(activity, connectionResult.errorMessage.toString(), Toast.LENGTH_SHORT).show() }.build()
        return googleApiClient
    }

    fun initialMapFragment(): MapFragment {
        val mapFragment: MapFragment = MapFragment.newInstance()
        (activity as AppCompatActivity).fragmentManager
                .beginTransaction()
                .replace(R.id.frame_google_map_api, mapFragment).commit()
        return mapFragment
    }
}