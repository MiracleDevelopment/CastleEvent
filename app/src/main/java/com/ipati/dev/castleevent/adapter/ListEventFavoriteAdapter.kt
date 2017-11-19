package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Fresco.loadPhoto
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import kotlinx.android.synthetic.main.custom_layout_list_favorite_adapter.view.*

class ListEventFavoriteAdapter(listItemEvent: ArrayList<ItemListEvent>) : RecyclerView.Adapter<ListEventFavoriteAdapter.ViewHolder>() {
    private var listFavoriteItemEvent: ArrayList<ItemListEvent> = listItemEvent

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_layout_list_favorite_adapter
                    , parent, false))

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int = listFavoriteItemEvent.count()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            setUpDetail()
        }

        @SuppressLint("SetTextI18n")
        private fun setUpDetail() {
            loadPhoto(context = itemView.context, url = listFavoriteItemEvent[adapterPosition].eventCover
                    , widthCardView = 250, heightCardView = 250, im = itemView.simple_drawee_logo_event)

            itemView.tv_name_event_favorite.text = listFavoriteItemEvent[adapterPosition].eventName
            itemView.tv_location_favorite.text = listFavoriteItemEvent[adapterPosition].eventLocation
            itemView.tv_time_favorite.text = listFavoriteItemEvent[adapterPosition].eventTime

            itemView.tv_count_favorite.text = "จำนวน ${listFavoriteItemEvent[adapterPosition].eventRest} คน"
        }
    }
}