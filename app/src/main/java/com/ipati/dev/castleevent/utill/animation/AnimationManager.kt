package com.ipati.dev.castleevent.utill.animation

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.ScaleAnimation
import com.ipati.dev.castleevent.ListDetailEventActivity
import kotlinx.android.synthetic.main.custom_list_event_adapter_layout.view.*


class AnimationManager(context: Context, activity: FragmentActivity) : Animator.AnimatorListener {
    private var mContext: Context = context
    private var mActivity: FragmentActivity = activity
    private var mMaxZ: Float = 50.0F
    private var mMaxY: Float = -5.0F
    private var mMin: Float = 0.0F
    private var mEventId: Long? = null
    private var mTransitionName: String? = null
    private var mWidth: Int? = null
    private var mHeight: Int? = null
    private var mItemView: View? = null
    private lateinit var mPropertyValueHolderZ: PropertyValuesHolder
    private lateinit var mValueAnimator: ValueAnimator

    fun setTransitionName(nameTransition: String) {
        mTransitionName = nameTransition
    }

    fun setEventId(eventId: Long) {
        mEventId = eventId
    }

    fun setViewAnimationTransitions(itemView: View?) {
        mItemView = itemView
    }

    fun setWidthView(width: Int) {
        mWidth = width
    }

    fun setHeightView(height: Int) {
        mHeight = height
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
            mItemView?.let {
                val intentAnimation = Intent(mContext, ListDetailEventActivity::class.java)

                val activityOptionsCompat: ActivityOptionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(mActivity
                                , mItemView!!.custom_im_cover_list_event
                                , ViewCompat.getTransitionName(mItemView))

                intentAnimation.putExtra("width", mWidth)
                intentAnimation.putExtra("height", mHeight)
                intentAnimation.putExtra("transitionName", mTransitionName)
                intentAnimation.putExtra("eventId", mEventId!!)
                mContext.startActivity(intentAnimation, activityOptionsCompat.toBundle())
            }
        }
    }

    override fun onAnimationStart(p0: Animator?) {

    }

    override fun onAnimationCancel(p0: Animator?) {

    }
}