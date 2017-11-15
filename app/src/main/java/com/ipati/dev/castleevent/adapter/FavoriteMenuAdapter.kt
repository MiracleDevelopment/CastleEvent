package com.ipati.dev.castleevent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.service.RecordedEvent.CategoryRecordData
import kotlinx.android.synthetic.main.custom_layout_favorite_adapter.view.*
import kotlin.collections.ArrayList


class FavoriteMenuAdapter(listItemFavorite: ArrayList<CategoryRecordData>) : RecyclerView.Adapter<FavoriteMenuAdapter.ViewHolder>() {
    val listItemFavoriteCategory: ArrayList<CategoryRecordData> = listItemFavorite
    var setOnRemoveItem: ((listItem: ArrayList<CategoryRecordData>, position: Int) -> Unit?)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_layout_favorite_adapter, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int = if (listItemFavoriteCategory.count() != 0) listItemFavoriteCategory[0].listCategory.count() else 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        private val listNameItem: Array<out String>? = itemView.resources.getStringArray(R.array.CategoryFavorite)

        override fun onLongClick(p0: View?): Boolean {
            setOnRemoveItem?.invoke(listItemFavoriteCategory, adapterPosition)
            return false
        }

        fun onBind() {
            listNameItem?.let {
                itemView.tv_item_category.text = listNameItem[listItemFavoriteCategory[0].listCategory[adapterPosition]]
                itemView.tv_item_category.setOnLongClickListener(this)
            }
        }
    }
}