package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.*
import kotlinx.android.synthetic.main.custom_list_category.view.*


class ListCategoryMenuAdapter(mListCategory: ArrayList<String>) : RecyclerView.Adapter<ListCategoryMenuAdapter.CategoryHolder>() {
    private var CATEGORY_ALL: String = "ALL"
    private var CATEGORY_EDUCATION: String = "Education"
    private var CATEGORY_TECHNOLOGY: String = "Technology"
    private var CATEGORY_SPORT: String = "Sport"

    var listCategory: ArrayList<String> = mListCategory
    private lateinit var mOnChangeCategory: LoadingCategory
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_category, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int = listCategory.count()

    fun setOnChangeCategory(loadingCategory: LoadingCategory) {
        this.mOnChangeCategory = loadingCategory
    }

    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.tv_category_bottom_sheet -> {
                    mOnChangeCategory.setOnChangeCategory(listCategory[adapterPosition])
                }
            }
        }

        fun onBind() {
            itemView.setOnClickListener { view ->
                onClick(view)
            }

            itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
            itemView.tv_category_bottom_sheet.setOnClickListener { view -> onClick(view) }

            onSetCountCategoryItem()
        }

        @SuppressLint("SetTextI18n")
        private fun onSetCountCategoryItem() {
            when (itemView.tv_category_bottom_sheet.text) {
                CATEGORY_ALL -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                }
                CATEGORY_EDUCATION -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                }
                CATEGORY_TECHNOLOGY -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                }
                CATEGORY_SPORT -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                }
            }
        }
    }
}