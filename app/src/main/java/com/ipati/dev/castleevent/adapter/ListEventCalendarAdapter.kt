package com.ipati.dev.castleevent.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.EventDetailModel


class ListEventCalendarAdapter(mListItemShow: ArrayList<EventDetailModel>) : RecyclerView.Adapter<ListEventCalendarAdapter.CalendarViewHolder>() {
    private var mListItem: ArrayList<EventDetailModel> = mListItemShow
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CalendarViewHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_event_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return mListItem.count()
    }


    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {

        }
    }
}