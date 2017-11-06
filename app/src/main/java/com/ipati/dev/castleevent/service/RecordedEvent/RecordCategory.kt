package com.ipati.dev.castleevent.service.RecordedEvent

import android.support.annotation.Keep
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ipati.dev.castleevent.model.UserManager.uidRegister


class RecordCategory {
    private var ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var refPushData: DatabaseReference = ref.child("userCategoryProfile").child(uidRegister)

    lateinit var categoryRecordData: CategoryRecordData

    fun pushProfile(gender: Int, listItemCategory: ArrayList<Int>): Task<Void> {
        categoryRecordData = CategoryRecordData(gender, listItemCategory)
        return refPushData.push().setValue(categoryRecordData)
    }

    @Keep
    data class CategoryRecordData(var gender: Int = 0, var listCategory: ArrayList<Int> = ArrayList())
}