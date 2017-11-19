package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.model.Fresco.loadPhoto
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import kotlinx.android.synthetic.main.custom_list_event_adapter_layout.view.*


class ExpireListEventAdapter(listItemListEvent: ArrayList<ItemListEvent>) : RecyclerView.Adapter<ExpireListEventAdapter.ViewHolder>() {
    private val listItemListEventExpire: ArrayList<ItemListEvent> = listItemListEvent

    var callBackExpireListEventAdapter: ((eventId: Long, width: Int, height: Int
                                          , transitionName: String
                                          , viewTransition: View
                                          , status: Int) -> Unit?)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_list_event_adapter_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int {
        return listItemListEventExpire.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener {
        private val gestureDetector: GestureDetector by lazy {
            GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    ViewCompat.setTransitionName(itemView.custom_im_cover_list_event, "${itemView.custom_im_cover_list_event.id}$adapterPosition")
                    callBackExpireListEventAdapter?.invoke(listItemListEventExpire[adapterPosition].eventId
                            , itemView.custom_im_cover_list_event.layoutParams.width
                            , itemView.custom_im_cover_list_event.layoutParams.height
                            , ViewCompat.getTransitionName(itemView.custom_im_cover_list_event)
                            , itemView.custom_im_cover_list_event
                            , 2)

                    return super.onSingleTapConfirmed(e)
                }
            })
        }

        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            p0?.performClick()
            return gestureDetector.onTouchEvent(p1)
        }

        fun onBind() {
            setUpDetail()
            setUpVisible()
        }

        private fun setUpVisible() {
            itemView.custom_im_cover_list_event.visibility = View.VISIBLE
        }

        @SuppressLint("SetTextI18n")
        private fun setUpDetail() {
            loadPhoto(itemView.context, listItemListEventExpire[adapterPosition].eventCover
                    , itemView.card_view_list_event.layoutParams.width, 350, itemView.custom_im_cover_list_event)

            itemView.custom_tv_header_list_event.text = listItemListEventExpire[adapterPosition].eventName
            itemView.custom_tv_location_list_event.text = listItemListEventExpire[adapterPosition].eventLocation
            itemView.custom_tv_time_list_event.text = listItemListEventExpire[adapterPosition].eventTime
            itemView.custom_tv_status_list_event.text = itemView.context.getString(R.string.expire_topic)

            itemView.custom_tv_people_count_list_event.text = "${itemView.context.getString(R.string.tv_count_people)} " +
                    "${listItemListEventExpire[adapterPosition].eventRest} ${itemView.context.getString(R.string.tv_people)}"

            itemView.constraint_space_content_layout.setOnTouchListener(this)
        }
    }
}