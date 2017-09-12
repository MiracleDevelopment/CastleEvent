package com.ipati.dev.castleevent.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ipati.dev.castleevent.CalendarActivity
import com.ipati.dev.castleevent.MyOrderActivity
import com.ipati.dev.castleevent.ProfileUserActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Glide.loadPhotoItemMenu
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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun onBind() {
            itemView.tv_item_menu_event.text = mListItemMenu[adapterPosition]
            itemView.tv_item_menu_event.setOnClickListener(this)
            iconItemMenu(adapterPosition)
        }

        private fun iconItemMenu(position: Int) {
            when (position) {
                0 -> {
                    loadPhotoItemMenu(itemView.context, R.mipmap.ic_person_pin, itemView.im_item_icon_menu_event)
                }
                1 -> {
                    loadPhotoItemMenu(itemView.context, R.mipmap.ic_list, itemView.im_item_icon_menu_event)
                }
                2 -> {
                    loadPhotoItemMenu(itemView.context, R.mipmap.ic_perm_contact_calendar, itemView.im_item_icon_menu_event)
                }
                3 -> {
                    loadPhotoItemMenu(itemView.context, R.mipmap.ic_contact_phone, itemView.im_item_icon_menu_event)
                }
            }

        }

        override fun onClick(p0: View?) {
            when {
                itemView.tv_item_menu_event.text == "My Profile" -> {
                    val intentProfile = Intent(p0?.context, ProfileUserActivity::class.java)
                    p0?.context?.startActivity(intentProfile)
                }
                itemView.tv_item_menu_event.text == "My Order" -> {
                    val intentMyOrder = Intent(p0?.context, MyOrderActivity::class.java)
                    p0?.context?.startActivity(intentMyOrder)
                }
                itemView.tv_item_menu_event.text == "Calendar" -> {
                    val intentCalendar = Intent(p0?.context, CalendarActivity::class.java)
                    p0?.context?.startActivity(intentCalendar)
                }
                else -> {
                    Toast.makeText(itemView.context, mListItemMenu[adapterPosition], Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}