package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.onShowToast
import com.ipati.dev.castleevent.model.Fresco.loadPhoto
import com.ipati.dev.castleevent.model.ModelListItem.ItemListEvent
import kotlinx.android.synthetic.main.custom_layout_list_favorite_adapter.view.*

class ListEventFavoriteAdapter(listItemEvent: ArrayList<ItemListEvent>) : RecyclerView.Adapter<ListEventFavoriteAdapter.ViewHolder>() {
    private var listFavoriteItemEvent: ArrayList<ItemListEvent> = listItemEvent
    var callBackItemCount: ((itemCount: Int) -> Unit?)? = null
    var callBackClickableItem: ((view: View, width: Int, height: Int, transitionName: String, eventId: Long, statusId: Int) -> Unit?)? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_layout_list_favorite_adapter
                    , parent, false))

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount(): Int {
        callBackItemCount?.invoke(listFavoriteItemEvent.count())
        return listFavoriteItemEvent.count()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener {
        private val gestureDetector: GestureDetector by lazy {
            GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    ViewCompat.setTransitionName(itemView.simple_drawee_logo_event
                            , "${itemView.simple_drawee_logo_event.id}$adapterPosition")

                    callBackClickableItem?.invoke(itemView.simple_drawee_logo_event
                            , itemView.simple_drawee_logo_event.layoutParams.width
                            , itemView.simple_drawee_logo_event.layoutParams.height
                            , ViewCompat.getTransitionName(itemView.simple_drawee_logo_event)
                            , listFavoriteItemEvent[adapterPosition].eventId, 0)

                    return super.onSingleTapUp(e)
                }
            })
        }

        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            p0?.performClick()
            return gestureDetector.onTouchEvent(p1)
        }

        fun onBind() {
            setUpDetail()
            setVisibleView()
        }

        private fun setVisibleView() {
            itemView.simple_drawee_logo_event.visibility = View.VISIBLE
        }

        @SuppressLint("SetTextI18n")
        private fun setUpDetail() {
            loadPhoto(context = itemView.context, url = listFavoriteItemEvent[adapterPosition].eventCover
                    , widthCardView = 250, heightCardView = 250, im = itemView.simple_drawee_logo_event)

            itemView.tv_name_event_favorite.text = listFavoriteItemEvent[adapterPosition].eventName
            itemView.tv_location_favorite.text = listFavoriteItemEvent[adapterPosition].eventLocation
            itemView.tv_time_favorite.text = listFavoriteItemEvent[adapterPosition].eventTime

            itemView.tv_count_favorite.text = "จำนวน ${listFavoriteItemEvent[adapterPosition].eventRest} คน"
            itemView.setOnTouchListener(this)
        }
    }
}