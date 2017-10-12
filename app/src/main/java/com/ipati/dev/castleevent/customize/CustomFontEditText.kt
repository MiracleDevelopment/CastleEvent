package com.ipati.dev.castleevent.customize

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet



class CustomFontEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        setUpFont(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setUpFont(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        setUpFont(context)
    }

    private fun setUpFont(mContext: Context) {
        typeface = Typeface.DEFAULT
    }
}