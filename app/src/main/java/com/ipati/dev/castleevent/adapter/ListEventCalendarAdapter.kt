package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.EventDetailModel
import kotlinx.android.synthetic.main.custom_list_event_calendar.view.*


class ListEventCalendarAdapter(mListItemShow: ArrayList<EventDetailModel>) : RecyclerView.Adapter<ListEventCalendarAdapter.CalendarViewHolder>() {
    private var mListItem: ArrayList<EventDetailModel> = mListItemShow
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CalendarViewHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_event_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = mListItem.count()


    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun onBind() {
            itemView.tv_custom_list_title_event_calendar.text = mListItem[adapterPosition].title
            itemView.tv_custom_list_date_calendar.text = mListItem[adapterPosition].timeEventStart + " น." + " - " + mListItem[adapterPosition].timeEventEnd + " น."
            itemView.tv_calendar_list_date.text = mListItem[adapterPosition].timeDayOfYear
            itemView.tv_calendar_list_month.text = mListItem[adapterPosition].timeMonthDate
        }
    }
}