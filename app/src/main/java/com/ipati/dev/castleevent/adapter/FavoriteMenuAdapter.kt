package com.ipati.dev.castleevent.adapter

import android.support.v7.widget.RecyclerView
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.service.RecordedEvent.CategoryRecordData
import kotlinx.android.synthetic.main.custom_layout_favorite_adapter.view.*
import kotlin.collections.ArrayList


class FavoriteMenuAdapter(listItemFavorite: ArrayList<CategoryRecordData>) : RecyclerView.Adapter<FavoriteMenuAdapter.ViewHolder>() {
    val listItemFavoriteCategory: ArrayList<CategoryRecordData> = listItemFavorite
    var setOnRemoveItem: ((listItem: ArrayList<CategoryRecordData>, position: Int) -> Unit?)? = null
    var setOnSingleTap: ((stringCategory: String) -> Unit?)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_layout_favorite_adapter, parent, false))

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int = if (listItemFavoriteCategory.count() != 0) listItemFavoriteCategory[0].listCategory.count() else 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        override fun onLongClick(p0: View?): Boolean {
            setOnRemoveItem?.invoke(listItemFavoriteCategory, adapterPosition)
            return false
        }

        override fun onClick(p0: View?) {
            setOnSingleTap?.invoke(p0?.tv_item_category?.text.toString())
        }

        private val listNameItem: Array<out String>? = itemView.resources.getStringArray(R.array.CategoryFavorite)


        fun onBind() {
            itemView.tv_item_category.text = listNameItem!![listItemFavoriteCategory[0].listCategory[adapterPosition]]
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }
}