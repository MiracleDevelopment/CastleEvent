package com.ipati.dev.castleevent.model

import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent

interface LoadingDialogListener {
    fun onPositiveClickable(statusLoading: Boolean)
    fun onNegativeClickable(statusLoading: Boolean)
}

interface LoadingTicketsEvent {
    fun onShowTicketsUser(eventId: String, userPhoto: String?, eventName: String, eventLogo: String, userAccount: String, eventLocation: String, count: Long)
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




