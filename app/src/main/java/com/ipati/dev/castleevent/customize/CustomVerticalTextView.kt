package com.ipati.dev.castleevent.customize

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.TextView

class CustomVerticalTextView : TextView {
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
        val typeface: Typeface = Typeface.createFromAsset(mContext.assets, "fonts/circular.ttf")
        setTypeface(typeface)
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