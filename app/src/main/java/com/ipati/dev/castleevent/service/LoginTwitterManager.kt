package com.ipati.dev.castleevent.service

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.ipati.dev.castleevent.authCredential.TwitterAuthCredential
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient

var consumerKEY: String? = "CONSUMER_KEY"
var consumerSecret: String? = "CONSUMER_SECRET"
var twitterLoginAuthentication: TwitterAuthClient = TwitterAuthClient()

fun TwitterConfig(context: Context) {
    val twitterConfig: TwitterConfig = TwitterConfig.Builder(context)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(TwitterAuthConfig(consumerKEY, consumerSecret))
            .debug(true)
            .build()
    Twitter.initialize(twitterConfig)

}

fun LoginTwitter(activity: FragmentActivity) {
    twitterLoginAuthentication.authorize(activity, object : Callback<TwitterSession>() {
        override fun success(result: Result<TwitterSession>?) {
            if (result != null) {
                TwitterAuthCredential(result.data)
            }
        }

        override fun failure(exception: TwitterException?) {
            Toast.makeText(activity, exception?.message.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}
