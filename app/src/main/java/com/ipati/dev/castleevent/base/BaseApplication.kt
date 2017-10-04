package com.ipati.dev.castleevent.base

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.database.FirebaseDatabase
import io.fabric.sdk.android.Fabric


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        Fabric.with(applicationContext, Crashlytics())
        Fresco.initialize(applicationContext)
    }
}