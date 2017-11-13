package com.ipati.dev.castleevent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent


class ExpireListEventAdapter(listItemListEvent: ArrayList<ItemListEvent>) : RecyclerView.Adapter<ExpireListEventAdapter.ViewHolder>() {
    private val listItemListEventExpire: ArrayList<ItemListEvent> = listItemListEvent

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_event_adapter_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

    }

    override fun getItemCount(): Int {
        return listItemListEventExpire.count()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {

        }
    }
}