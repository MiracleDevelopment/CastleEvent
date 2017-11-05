package com.ipati.dev.castleevent.utill

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation


class CustomHeightViewCollapse(view: View, height: Int, mHeightFromView: Int) : Animation() {
    private var view: View? = null
    private var specificHeightView: Int? = null
    private var originalHeightView: Int? = null
    private var configHeight: Int? = null

    init {
        this.view = view
        this.specificHeightView = mHeightFromView
        this.originalHeightView = height
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        if (view?.height != originalHeightView) {
            configHeight = (specificHeightView!! + (originalHeightView!! - specificHeightView!!) * interpolatedTime).toInt()
            view?.layoutParams?.height = configHeight
            view?.requestLayout()
        }
    }


    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}