package com.ipati.dev.castleevent.model.gmsLocation

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient

class GooglePlayServiceMapManager(activity: Activity, lifecycle: Lifecycle) : LifecycleObserver {
    private var mLifecycle: Lifecycle? = null
    private var activity: Activity? = null

    private lateinit var mGoogleApiClient: GoogleApiClient

    init {
        this.activity = activity
        this.mLifecycle = lifecycle
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        initialGoogleApiClient()
    }

    private fun initialGoogleApiClient(): GoogleApiClient {
        mGoogleApiClient = GoogleApiClient.Builder(activity!!)
                .addOnConnectionFailedListener { connectionResult ->
                    Toast.makeText(activity, connectionResult.errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }.build()
        return mGoogleApiClient
    }
}