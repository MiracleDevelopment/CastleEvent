package com.ipati.dev.castleevent.model


interface LoadingListener {
    fun onShowLoading(statusLoading: Boolean)
    fun onHindLoading(statusLoading: Boolean)
}

interface ShowListEventFragment {
    fun onShowListFragment()
}