package com.ipati.dev.castleevent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ipati.dev.castleevent.R
import kotlinx.android.synthetic.main.custom_list_category.view.*


class ListCategoryMenuAdapter(mListCategory: ArrayList<String>) : RecyclerView.Adapter<ListCategoryMenuAdapter.CategoryHolder>() {
    var listCategory: ArrayList<String> = mListCategory

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_category, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int {
        return listCategory.count()
    }

    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.tv_category_bottom_sheet -> {
                    Toast.makeText(itemView.context, "Clickable", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun onBind() {
            itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
            itemView.setOnClickListener { view -> onClick(view) }
        }
    }
}