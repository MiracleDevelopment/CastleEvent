package com.ipati.dev.castleevent.service.RecordedEvent

import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.util.*

class RecordCategory {
    private var ref: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun pushProfile(uid: String?, listItemCategory: ArrayList<Int>): Task<Void>? {
        val refPushData: DatabaseReference = ref.child("userCategoryProfile").child(uid)
        val categoryData = CategoryRecordData(listCategory = listItemCategory)

        return refPushData.push().setValue(categoryData)
    }
}