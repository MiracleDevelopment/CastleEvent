package com.ipati.dev.castleevent.utill.animation

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.view.View
import com.ipati.dev.castleevent.ListDetailEventActivity


class AnimationManager(context: Context) : Animator.AnimatorListener {
    private var mContext: Context = context
    private var mMaxZ: Float = 50.0F
    private var mMaxY: Float = -5.0F
    private var mMin: Float = 0.0F
    private var mEventId: Long? = null

    private lateinit var mPropertyValueHolderZ: PropertyValuesHolder
    private lateinit var mValueAnimator: ValueAnimator

    fun setEventId(eventId: Long) {
        mEventId = eventId
    }

    fun onLoadingTranslateZ(): ValueAnimator {
        mPropertyValueHolderZ = PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, mMin, mMaxZ)
        mValueAnimator = ValueAnimator.ofPropertyValuesHolder(mPropertyValueHolderZ)
        mValueAnimator.addListener(this)
        if (!mValueAnimator.isStarted) {
            mValueAnimator.start()
        } else {
            mValueAnimator.cancel()
        }
        return mValueAnimator
    }

    fun onLoadingTranslateY(): ValueAnimator {
        mPropertyValueHolderZ = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, mMin, mMaxY)
        mValueAnimator = ValueAnimator.ofPropertyValuesHolder(mPropertyValueHolderZ)
        if (!mValueAnimator.isStarted) {
            mValueAnimator.start()
        } else {
            mValueAnimator.cancel()
        }
        mValueAnimator.start()
        return mValueAnimator
    }

    override fun onAnimationRepeat(p0: Animator?) {

    }

    override fun onAnimationEnd(p0: Animator?) {
        mEventId?.let {
            val intentAnimation = Intent(mContext, ListDetailEventActivity::class.java)
            intentAnimation.putExtra("eventId", mEventId!!)
            mContext.startActivity(intentAnimation)
        }
    }

    override fun onAnimationStart(p0: Animator?) {

    }

    override fun onAnimationCancel(p0: Animator?) {

    }
}