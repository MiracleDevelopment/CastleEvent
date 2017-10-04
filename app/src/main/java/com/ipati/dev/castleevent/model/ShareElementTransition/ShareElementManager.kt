package com.ipati.dev.castleevent.model.ShareElementTransition

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewTreeObserver


class ShareElementManager(context: Context) {
    var mContext: Context = context

    fun scheduleViewTreeObserver(activity: FragmentActivity, shareElement: View) {
        shareElement.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                shareElement.viewTreeObserver.removeOnPreDrawListener(this)
                activity.supportStartPostponedEnterTransition()
                return true
            }
        })
    }
}