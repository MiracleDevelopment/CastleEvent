package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.getStringResource
import com.ipati.dev.castleevent.model.Fresco.loadPhoto
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import kotlinx.android.synthetic.main.custom_list_event_adapter_layout.view.*


class ListEventAdapter(listItem: ArrayList<ItemListEvent>) : RecyclerView.Adapter<ListEventAdapter.ListEventHolder>() {
    var itemList: ArrayList<ItemListEvent> = listItem
    var itemViewTransition: View? = null
    var onItemTransitionClickable: ((view: View?, width: Int, height: Int, transitionName: String, eventId: Long) -> Unit)? = null
    override fun getItemCount(): Int = itemList.count()


    override fun onBindViewHolder(holder: ListEventHolder?, position: Int) {
        holder?.onBind(itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListEventHolder {
        val view: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.custom_list_event_adapter_layout
                        , parent, false)

        return ListEventHolder(view)
    }

    inner class ListEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
            , View.OnTouchListener {

        private val gestureDetector: GestureDetector by lazy {
            GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    itemViewTransition?.let {
                        val widthOriginal: Int = itemViewTransition?.custom_im_cover_list_event!!.measuredWidth
                        val heightOriginal: Int = itemViewTransition?.custom_im_cover_list_event!!.measuredHeight

                        ViewCompat.setTransitionName(itemViewTransition?.custom_im_cover_list_event
                                , "${itemViewTransition!!.custom_im_cover_list_event.id}$adapterPosition")


                        onItemTransitionClickable?.invoke(itemViewTransition?.custom_im_cover_list_event
                                , widthOriginal
                                , heightOriginal
                                , ViewCompat.getTransitionName(itemViewTransition?.custom_im_cover_list_event)
                                , itemList[adapterPosition].eventId)

                    }
                    return super.onSingleTapConfirmed(e)
                }
            })
        }

        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            p0?.performClick()
            getViewSelect(p0!!)
            return gestureDetector.onTouchEvent(p1)
        }


        @SuppressLint("SetTextI18n")
        fun onBind(itemList: ArrayList<ItemListEvent>) {
            loadPhoto(itemView.context, itemList[adapterPosition].eventCover,itemView.card_view_list_event.layoutParams.width, itemView.custom_im_cover_list_event)
            itemView.custom_tv_header_list_event.text = itemList[adapterPosition].eventName

            itemView.custom_tv_location_list_event.text = itemList[adapterPosition].eventLocation
            itemView.custom_tv_time_list_event.text = itemList[adapterPosition].eventTime

            itemView.custom_tv_people_count_list_event.text = itemView.getStringResource(R.string.tv_count_people) +
                    " " + itemList[adapterPosition].eventRest.toString() +
                    " " + itemView.getStringResource(R.string.tv_people)

            setVisibleView(itemView.custom_im_cover_list_event)
            setStatusEvent(itemView, itemList[adapterPosition].eventStatus)

            itemView.constraint_space_content_layout.setOnTouchListener(this)
        }

        private fun setVisibleView(appearanceView: View) {
            if (appearanceView.visibility == View.INVISIBLE) {
                appearanceView.visibility = View.VISIBLE
            }
        }

        private fun setStatusEvent(statusView: View, status: Boolean) {
            if (status) {
                statusView.custom_tv_status_list_event.text = itemView.getStringResource(R.string.tv_status_open)
            } else {
                statusView.custom_tv_status_list_event.text = itemView.getStringResource(R.string.tv_status_close)
            }
        }


        private fun getViewSelect(itemView: View) {
            itemViewTransition = itemView
        }
    }

}