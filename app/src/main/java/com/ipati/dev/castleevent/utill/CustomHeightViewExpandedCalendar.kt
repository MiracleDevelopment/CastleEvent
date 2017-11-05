package com.ipati.dev.castleevent.utill

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation


class CustomHeightViewExpandedCalendar(view: View, height: Int, viewHeight: Int) : Animation() {
    private var view: View? = null
    private var specificHeightView: Int? = null
    private var originalHeightView: Int? = null
    private var calculateAnimation: Int? = null

    init {
        this.view = view
        this.specificHeightView = height
        this.originalHeightView = viewHeight
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        if (originalHeightView!! < specificHeightView!!) {
            calculateAnimation = (originalHeightView!! + (specificHeightView!! - originalHeightView!!) * interpolatedTime).toInt()
            if (calculateAnimation != 0) {
                view?.layoutParams?.height = calculateAnimation
                view?.requestLayout()
            }
        }
    }

    override fun willChangeBounds(): Boolean {
        return true
    }


}