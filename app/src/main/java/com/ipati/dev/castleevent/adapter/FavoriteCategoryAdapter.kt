package com.ipati.dev.castleevent.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.custom_layout_favorite_category.view.*

class FavoriteCategoryAdapter(context: Context) : RecyclerView.Adapter<FavoriteCategoryAdapter.FavoriteHolder>() {
    private val itemCategoryFavorite: Array<out String> = context.resources.getStringArray(R.array.CategoryFavorite)
    var onSelectorItemCategory: ((view: View, itemPosition: Int) -> Unit)? = null
    override fun onBindViewHolder(holder: FavoriteHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FavoriteHolder {
        return FavoriteHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_layout_favorite_category
                , parent
                , false))
    }

    override fun getItemCount(): Int {
        return itemCategoryFavorite.count()
    }

    inner class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {
            p0?.let {
                onSelectorItemCategory?.invoke(p0, adapterPosition)
            }
        }

        fun onBind() {
            if (adapterPosition != -1) {
                itemView.tv_item_category_favorite.tag = adapterPosition.toString()
                itemView.tv_item_category_favorite.setOnClickListener(this)
                setTextGiveItem()
            }
        }

        private fun setTextGiveItem() {
            itemView.tv_item_category_favorite.text = itemCategoryFavorite[adapterPosition]
        }
    }
}