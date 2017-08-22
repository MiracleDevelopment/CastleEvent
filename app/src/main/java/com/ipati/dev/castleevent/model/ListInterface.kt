package com.ipati.dev.castleevent.model

import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent


interface LoadingListener {
    fun onShowLoading(statusLoading: Boolean)
    fun onHindLoading(statusLoading: Boolean)
}

interface ShowListEventFragment {
    fun onShowListFragment()
}


interface LoadingDetailData {
    fun onLoadingUpdateData(itemListEvent: ItemListEvent)
}
