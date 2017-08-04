package com.ipati.dev.castleevent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R


class ListEventAdapter : RecyclerView.Adapter<ListEventAdapter.ListEventHolder>() {
    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ListEventHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListEventHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_event_adapter_layout, parent, false)
        return ListEventHolder(view)
    }

    class ListEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {

        }
    }
}