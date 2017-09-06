package com.ipati.dev.castleevent.model

import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent


interface LoadingDialogListener {
    fun onPositiveClickable(statusLoading: Boolean)
    fun onNegativeClickable(statusLoading: Boolean)
}

interface ShowListEventFragment {
    fun onShowListFragment()
}


interface LoadingDetailData {
    fun onLoadingUpdateData(itemListEvent: ItemListEvent)
}
