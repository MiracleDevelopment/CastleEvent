package com.ipati.dev.castleevent.utill

import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class CustomHeightViewCollapseCalendar(view: View, height: Int, viewHeight: Int) : Animation() {
    private var mView: View? = null
    private var mHeight: Int? = null
    private var mViewHeight: Int? = null
    private var mCalculateAnimation: Int? = null

    init {
        this.mView = view
        this.mHeight = height
        this.mViewHeight = viewHeight
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        if (mViewHeight!! > mHeight!!) {
            mCalculateAnimation = (mViewHeight!! + (mHeight!! - mViewHeight!!) * interpolatedTime).toInt()
            Log.d("collapse", mCalculateAnimation.toString())
            mView?.layoutParams?.height = mCalculateAnimation
            mView?.requestLayout()
        }
    }

    override fun willChangeBounds(): Boolean {
        return true
    }

}