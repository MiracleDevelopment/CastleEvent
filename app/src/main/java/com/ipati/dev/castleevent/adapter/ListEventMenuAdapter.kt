package com.ipati.dev.castleevent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.custom_layout_menu_event_adapter.view.*

class ListEventMenuAdapter(listItemMenu: ArrayList<String>) : RecyclerView.Adapter<ListEventMenuAdapter.ViewHolder>() {
    private var mListItemMenu: ArrayList<String> = listItemMenu

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int {
        return mListItemMenu.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_layout_menu_event_adapter
                , parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            itemView.tv_item_menu_event.text = mListItemMenu[adapterPosition]
        }
    }
}