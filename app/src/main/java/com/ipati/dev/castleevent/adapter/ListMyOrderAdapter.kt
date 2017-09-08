package com.ipati.dev.castleevent.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.custom_list_item_my_order.view.*

class ListMyOrderAdapter(context: Context) : RecyclerView.Adapter<ListMyOrderAdapter.ListMyOderHolder>() {
    override fun onBindViewHolder(holder: ListMyOderHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListMyOderHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_item_my_order, parent, false)
        return ListMyOderHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ListMyOderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            itemView.title_list_my_order_event.text = "Castle Pass" + " #" + (adapterPosition + 1).toString()
        }
    }
}