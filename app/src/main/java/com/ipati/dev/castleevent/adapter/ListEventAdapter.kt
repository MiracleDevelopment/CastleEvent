package com.ipati.dev.castleevent.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.ipati.dev.castleevent.R
import com.ipati.dev.castleevent.extension.getStringResource
import com.ipati.dev.castleevent.model.Glide.loadPhoto
import com.ipati.dev.castleevent.model.LoadingDetailEvent
import com.ipati.dev.castleevent.model.OnCancelAnimationTouch
import com.ipati.dev.castleevent.model.modelListEvent.ItemListEvent
import kotlinx.android.synthetic.main.custom_list_event_adapter_layout.view.*


class ListEventAdapter(listItem: ArrayList<ItemListEvent>) : RecyclerView.Adapter<ListEventAdapter.ListEventHolder>() {
    var itemList: ArrayList<ItemListEvent> = listItem
    lateinit var animatorListItem: Animation
    lateinit var mOnLoadingDetailEvent: LoadingDetailEvent
    lateinit var mOnCancelAnimation: OnCancelAnimationTouch
    lateinit var mRippleDrawable: RippleDrawable

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: ListEventHolder?, position: Int) {
        holder?.onBind(itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListEventHolder {
        val view: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.custom_list_event_adapter_layout
                        , parent, false)

        return ListEventHolder(view)
    }

    fun setOnClickItemEvent(loadingDetailEvent: LoadingDetailEvent) {
        this.mOnLoadingDetailEvent = loadingDetailEvent
    }


    fun setOnCancelTouchItemEvent(onCancelAnimationTouch: OnCancelAnimationTouch) {
        this.mOnCancelAnimation = onCancelAnimationTouch
    }

    inner class ListEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
            , View.OnTouchListener {

        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            p0?.performClick()
            if (p0?.id == R.id.ripple_background) {
                mRippleDrawable = p0.background as RippleDrawable
                mRippleDrawable.setHotspot(p1?.x!!, p1.y)
                mRippleDrawable.setColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.colorRipple)))

                when (p1.action) {
                    MotionEvent.ACTION_UP -> {
                        val widthOriginal: Int = itemView.custom_im_cover_list_event.measuredWidth
                        val heightOriginal: Int = itemView.custom_im_cover_list_event.measuredHeight

                        ViewCompat.setTransitionName(p0.custom_im_cover_list_event, "${p0.id}$adapterPosition")
                        mOnCancelAnimation.setOnCancelTouch(p0.custom_im_cover_list_event
                                , widthOriginal
                                , heightOriginal
                                , ViewCompat.getTransitionName(p0.custom_im_cover_list_event)
                                , itemList[adapterPosition].eventId)
                    }

                    MotionEvent.ACTION_DOWN -> {
                        if (p1.pointerCount == 1) {
                            mOnLoadingDetailEvent.setOnLoadingDetailEvent(p0.custom_im_cover_list_event, itemList[adapterPosition].eventId)
                        }
                    }
                }
            }
            return false
        }


        @SuppressLint("SetTextI18n")
        fun onBind(itemList: ArrayList<ItemListEvent>) {
            loadPhoto(itemView.context, itemList[adapterPosition].eventCover, itemView.custom_im_cover_list_event)

            itemView.custom_tv_header_list_event.text = itemList[adapterPosition].eventName

            itemView.custom_tv_location_list_event.text = itemList[adapterPosition].eventLocation
            itemView.custom_tv_time_list_event.text = itemList[adapterPosition].eventTime

            itemView.custom_tv_people_count_list_event.text = itemView.getStringResource(R.string.tv_count_people) +
                    " " + itemList[adapterPosition].eventRest.toString() +
                    " " + itemView.getStringResource(R.string.tv_people)

//            animationListItem().start()

            if (itemList[adapterPosition].eventStatus) {
                itemView.custom_tv_status_list_event.text = itemView.getStringResource(R.string.tv_status_open)
            } else {
                itemView.custom_tv_status_list_event.text = itemView.getStringResource(R.string.tv_status_close)
            }

            itemView.ripple_background.translationY = 0.0F
            itemView.ripple_background.translationZ = 0.0F
            itemView.ripple_background.setOnTouchListener { view, motionEvent -> onTouch(view, motionEvent) }

            if (itemView.custom_im_cover_list_event.visibility == View.INVISIBLE) {
                itemView.custom_im_cover_list_event.visibility = View.VISIBLE
            }

        }


        private fun animationListItem(): Animation {
            animatorListItem = AnimationUtils.loadAnimation(itemView.context, R.anim.fade_item_translation)
            itemView.animation = animatorListItem
            animatorListItem.start()
            return animatorListItem
        }
    }
}