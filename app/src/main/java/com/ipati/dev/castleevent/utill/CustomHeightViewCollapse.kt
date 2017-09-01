package com.ipati.dev.castleevent.utill

import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation


class CustomHeightViewCollapse(view: View, height: Int, mHeightFromView: Int) : Animation() {
    private var mView: View? = null
    private var mHeightFromView: Int? = null
    private var mHeight: Int? = null
    private var mHeightConfig: Int? = null

    init {
        this.mView = view
        this.mHeightFromView = mHeightFromView
        this.mHeight = height
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        if (mView?.height != mHeight) {
            mHeightConfig = (mHeightFromView!! + (mHeight!! - mHeightFromView!!) * interpolatedTime).toInt()
            mView?.layoutParams?.height = mHeightConfig
            mView?.requestLayout()
        }
    }


    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}