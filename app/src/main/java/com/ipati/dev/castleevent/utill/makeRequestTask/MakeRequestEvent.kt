package com.ipati.dev.castleevent.utill.makeRequestTask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.calendar.model.Event


class MakeRequestEvent : AsyncTask<Void, Void, Event>() {
    private lateinit var event: Event
    private var exception: Exception? = null
    private var transpot: HttpTransport = AndroidHttp.newCompatibleTransport()
    private var mJsonFactory: JacksonFactory = JacksonFactory.getDefaultInstance()
    private var service: com.google.api.services.calendar.Calendar = com.google.api.services.calendar.Calendar
            .Builder(transpot, mJsonFactory, null).build()

    override fun doInBackground(vararg p0: Void?): Event? {
        return try {
            eventCalendar()
        } catch (e: Exception) {
            exception = e
            cancel(true)
            null
        }
    }

    private fun eventCalendar(): Event {
        event = service.events().get("primary", "eventId").execute()
        return event
    }

    override fun onPostExecute(result: Event?) {
        super.onPostExecute(result)
        Log.d("resultExecute", result.toString())
    }

    override fun onCancelled() {
        super.onCancelled()
        if (exception != null) {
            when (exception) {
                is UserRecoverableAuthIOException -> {
                    Log.d("error", exception?.message.toString())
                }
                is GooglePlayServicesAvailabilityIOException -> {
                    Log.d("error", exception?.message.toString())
                }
                else -> {
                    Log.d("error", exception?.message.toString())
                }
            }
        }
    }
}