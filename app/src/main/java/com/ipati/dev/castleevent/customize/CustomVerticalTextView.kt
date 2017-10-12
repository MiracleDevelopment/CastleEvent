package com.ipati.dev.castleevent.customize

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatTextView
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.TextView
import com.ipati.dev.castleevent.R

class CustomVerticalTextView : AppCompatTextView {
    private var mReact: Rect = Rect()
    private var mPaint: TextPaint = paint

    constructor(context: Context) : super(context) {
        setUpTypeFace(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setUpTypeFace(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        setUpTypeFace(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
        includeFontPadding = false
    }

    private fun setUpTypeFace(mContext: Context) {
        typeface = ResourcesCompat.getFont(mContext, R.font.circular)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        canvas.translate(measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.rotate(270F)

        mPaint.color = textColors.defaultColor
        mPaint.getTextBounds(text as String?, 0, text.length, mReact)

        canvas.drawText(text as String, compoundPaddingLeft.toFloat(), (mReact.height() - measuredWidth) / 2.5.toFloat(), mPaint)
        canvas.restore()

    }
}