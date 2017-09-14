package com.ipati.dev.castleevent.extension

import android.widget.EditText


fun EditText.toStrEditText(): String {
    return text.toString()
}