package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Glide.loadPhoto
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent
import kotlinx.android.synthetic.main.custom_list_event_adapter_layout.view.*


class ListEventAdapter(listItem: ArrayList<ItemListEvent>) : RecyclerView.Adapter<ListEventAdapter.ListEventHolder>() {
    var itemList: ArrayList<ItemListEvent> = listItem
    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: ListEventHolder?, position: Int) {
        holder?.onBind(itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListEventHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_event_adapter_layout, parent, false)
        return ListEventHolder(view)
    }

    class ListEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {

        }

        @SuppressLint("SetTextI18n")
        fun onBind(itemList: ArrayList<ItemListEvent>) {
            loadPhoto(itemView.context, itemList[adapterPosition].eventCover, itemView.custom_im_cover_list_event)

            itemView.custom_tv_header_list_event.text = itemList[adapterPosition].eventName

            itemView.custom_tv_location_list_event.text = itemList[adapterPosition].eventLocation
            itemView.custom_tv_time_list_event.text = itemList[adapterPosition].eventTime

            itemView.custom_tv_people_count_list_event.text = itemView.resources.getString(R.string.tv_count_people) +
                    " " + itemList[adapterPosition].eventMax.toString() +
                    " " + itemView.context.resources.getString(R.string.tv_people)

            if (itemList[adapterPosition].eventStatus) {
                itemView.custom_tv_status_list_event.text = itemView.context.resources.getString(R.string.tv_status_open)

            } else {
                itemView.custom_tv_status_list_event.text = itemView.context.resources.getString(R.string.tv_status_close)

            }

            itemView.setOnClickListener { view -> onClick(view) }
        }
    }
}