package com.ipati.dev.castleevent.utill

import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation


class CustomHeightViewExpandedCalendar(view: View, height: Int, viewHeight: Int) : Animation() {
    private var view: View? = null
    private var mHeight: Int? = null
    private var mViewHeight: Int? = null
    private var mCalculateAnimation: Int? = null

    init {
        this.view = view
        this.mHeight = height
        this.mViewHeight = viewHeight
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        if (mViewHeight!! < mHeight!!) {
            mCalculateAnimation = (mViewHeight!! + (mHeight!! - mViewHeight!!) * interpolatedTime).toInt()
            if (mCalculateAnimation != 0) {
                Log.d("expanded", mCalculateAnimation.toString())
                view?.layoutParams?.height = mCalculateAnimation
                view?.requestLayout()
            }
        }
    }

    override fun willChangeBounds(): Boolean {
        return true
    }


}