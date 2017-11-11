package com.ipati.dev.castleevent.model.UserManager

import android.content.Context
import android.text.Editable
import android.text.TextWatcher


class EditableChangeText(private val context: Context
                         , private val listEditText: ArrayList<DataEditText>
                         , private var callBackEnable: (Boolean) -> Unit) : TextWatcher {

    fun addOnChangeTextListener() {
        listEditText.forEach { it.editText.addTextChangedListener(this)}
        }

        override fun afterTextChanged(p0: Editable?) {
            callBackEnable(listEditText.any { dataEditText -> dataEditText.editText.text.toString() != dataEditText.message })
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }