package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.getStringResource
import com.ipati.dev.castleevent.model.*
import kotlinx.android.synthetic.main.custom_list_category.view.*


class ListCategoryMenuAdapter(mListCategory: ArrayList<String>) : RecyclerView.Adapter<ListCategoryMenuAdapter.CategoryHolder>() {
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
        private var categoryAll: String = itemView.getStringResource(R.string.categoryAll)
        private var categoryEducation: String = itemView.getStringResource(R.string.categoryEducation)
        private var categoryTechnology: String = itemView.getStringResource(R.string.categoryTechnology)
        private var categorySport: String = itemView.getStringResource(R.string.categorySport)

        override fun onClick(p0: View?) {
            mOnChangeCategory.setOnChangeCategory(listCategory[adapterPosition])
        }

        fun onBind() {
            itemView.setOnClickListener { view -> onClick(view) }

            itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]

            onSetCountCategoryItem()
        }

        @SuppressLint("SetTextI18n")
        private fun onSetCountCategoryItem() {
            when (itemView.tv_category_bottom_sheet.text) {
                categoryAll -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                    itemView.im_icon_category.setImageResource(R.mipmap.ic_all_inclusive)
                }
                categoryEducation -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                    itemView.im_icon_category.setImageResource(R.mipmap.ic_school)

                }
                categoryTechnology -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                    itemView.im_icon_category.setImageResource(R.mipmap.ic_computer)
                }
                categorySport -> {
                    itemView.tv_category_bottom_sheet.text = listCategory[adapterPosition]
                    itemView.im_icon_category.setImageResource(R.mipmap.ic_directions_run)
                }
            }
        }
    }
}