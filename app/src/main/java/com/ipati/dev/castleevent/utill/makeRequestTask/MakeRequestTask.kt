package com.ipati.dev.castleevent.utill.makeRequestTask

import android.os.AsyncTask
import android.util.Log
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Events
import java.util.ArrayList


class MakeRequestTask(mGoogleCredentialAccount: GoogleAccountCredential) : AsyncTask<Void, Void, List<String>>() {
    private var mService: Calendar? = null
    private var mListError: Exception? = null
    private var dateTimeStart: DateTime? = null
    private lateinit var mDateTimeNow: DateTime
    private lateinit var eventListString: ArrayList<String>
    private lateinit var mEvents: Events
    private lateinit var mListEvent: List<Event>
    private var transport: HttpTransport = AndroidHttp.newCompatibleTransport()
    private var jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

    init {
        mService = Calendar.Builder(transport, jsonFactory, mGoogleCredentialAccount)
                .setApplicationName("Google Calendar Android QuickStart")
                .build()
    }

    override fun doInBackground(vararg p0: Void?): List<String>? {
        return try {
            getDataFromApi()
        } catch (e: Exception) {
            mListError = e
            cancel(true)
            null
        }
    }

    private fun getDataFromApi(): List<String> {
        mDateTimeNow = DateTime(System.currentTimeMillis())
        eventListString = ArrayList()
        mEvents = mService?.events()?.list("primary")
                ?.setMaxResults(10)
                ?.setTimeMin(mDateTimeNow)
                ?.setOrderBy("startTime")
                ?.setSingleEvents(true)
                ?.execute()!!

        mListEvent = mEvents.items

        for (item in mListEvent) {
            dateTimeStart = item.start.dateTime
            if (dateTimeStart == null) {
                dateTimeStart = item.start.date
            }
            eventListString.add(String.format("%s (%s)", item.summary, dateTimeStart))
        }

        return eventListString
    }

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d("AsyncTaskPre", "start")
    }

    override fun onPostExecute(result: List<String>?) {
        super.onPostExecute(result)
        Log.d("resultAsyncTask", result!![0])
    }

    override fun onCancelled() {
        super.onCancelled()
        if (mListError != null) {
            when (mListError) {
                is GooglePlayServicesAvailabilityIOException -> {
                    Log.d("AsyncTaskError", "GooglePlayServicesAvailability")
                }
                is UserRecoverableAuthIOException -> {
                    Log.d("AsyncTaskError", "UserRecoverable")
                }
                else -> {
                    Log.d("AsyncTaskError", mListError.toString())
                }
            }
        }
    }
}