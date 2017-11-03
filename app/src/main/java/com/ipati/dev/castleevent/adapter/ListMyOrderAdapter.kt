package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.*
import com.ipati.dev.castleevent.MyOrderActivity
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Fresco.loadPhotoTickets
import com.ipati.dev.castleevent.model.LoadingTicketsEvent
import com.ipati.dev.castleevent.model.History.RecorderTickets
import kotlinx.android.synthetic.main.custom_list_item_my_order.view.*
import java.util.*

class ListMyOrderAdapter(mListOrder: ArrayList<RecorderTickets>) : RecyclerView.Adapter<ListMyOrderAdapter.ListMyOderHolder>() {
    var mListItem: ArrayList<RecorderTickets> = mListOrder
    lateinit var onShowTicketsDialog: LoadingTicketsEvent
    lateinit var mCalendar: Calendar

    override fun onBindViewHolder(holder: ListMyOderHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListMyOderHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_item_my_order, parent, false)
        return ListMyOderHolder(view)
    }

    override fun getItemCount(): Int = mListItem.count()

    inner class ListMyOderHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener {

        private val gestureDetector: GestureDetector by lazy {
            GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    onQrInformationSend()
                    return super.onSingleTapConfirmed(e)
                }
            })
        }

        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            p0?.performClick()
            gestureDetector.onTouchEvent(p1)
            return false
        }


        @SuppressLint("SetTextI18n")
        fun onBind() {
            itemView.title_list_my_order_event.text = "Castle Event" + " #" + (adapterPosition + 1).toString()
            itemView.tv_title_order_name_event.text = mListItem[adapterPosition].eventName
            itemView.tv_order_location.text = mListItem[adapterPosition].eventLocation
            itemView.tv_order_date_time_buy.text = mListItem[adapterPosition].dateStamp
            itemView.tv_count_people_tickets.text = mListItem[adapterPosition].count.toString()
            itemView.tv_get_qr_code.setOnTouchListener(this)
            itemView.my_order_view_list.setOnTouchListener(this)

            loadPhotoTickets(itemView.context, mListItem[adapterPosition].eventLogo, itemView.im_photo_order_event)
            onDateConfig(itemView)
        }

        private fun onDateConfig(itemView: View) {
            mCalendar = Calendar.getInstance()
            mCalendar.timeInMillis = mListItem[adapterPosition].timeStamp
            val displayDay = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale("th"))
            when (displayDay) {
                "วันจันทร์" -> {
                    itemView.title_list_my_order_event.setBackgroundResource(R.drawable.custom_back_ground_title_my_order_monday)
                }
                "วันอังคาร" -> {
                    itemView.title_list_my_order_event.setBackgroundResource(R.drawable.custom_back_ground_title_my_order_tuesday)
                }
                "วันพุธ" -> {
                    itemView.title_list_my_order_event.setBackgroundResource(R.drawable.custom_back_ground_title_my_order_wednesday)
                }
                "วันพฤหัสบดี" -> {
                    itemView.title_list_my_order_event.setBackgroundResource(R.drawable.custom_back_ground_title_my_order_thuesday)
                }
                "วันศุกร์" -> {
                    itemView.title_list_my_order_event.setBackgroundResource(R.drawable.custom_back_ground_title_my_order_friday)
                }
                "วันเสาร์" -> {
                    itemView.title_list_my_order_event.setBackgroundResource(R.drawable.custom_back_ground_title_my_order_saturday)
                }
                "วันอาทิตย์" -> {
                    itemView.title_list_my_order_event.setBackgroundResource(R.drawable.custom_back_ground_title_my_order_sunday)
                }
            }
        }

        private fun onQrInformationSend() {
            onShowTicketsDialog = itemView.context as MyOrderActivity
            onShowTicketsDialog.onShowTicketsUser(mListItem[adapterPosition].eventId
                    , mListItem[adapterPosition].eventLogo
                    , mListItem[adapterPosition].eventName
                    , mListItem[adapterPosition].userAccount
                    , mListItem[adapterPosition].eventLocation
                    , mListItem[adapterPosition].count)
        }
    }

}