package com.ipati.dev.castleevent.model

import android.view.View
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent

interface LoadingDialogListener {
    fun onPositiveClickable(statusLoading: Boolean)
    fun onNegativeClickable(statusLoading: Boolean)
}

interface LoadingTicketsEvent {
    fun onShowTicketsUser(eventId: String, eventPhoto: String?, eventName: String, userAccount: String, eventLocation: String, count: Long)
}


interface LoadingDetailData {
    fun onLoadingUpdateData(itemListEvent: ItemListEvent)
}

interface DismissDialogFragment {
    fun onChangeProfile(msg: String, requestCode: Int)
}

interface LoadingCategory {
    fun setOnChangeCategory(selectCategory: String)
}

interface OnUpdateInformation {
    fun setDataChange(itemListEvent: ItemListEvent)
}

interface OnProgressPhotoUser {
    fun setProgressUserPhoto(progress: Int)
}


interface OnMissingConfirm {
    fun onMissingDialogConfirm()
}





