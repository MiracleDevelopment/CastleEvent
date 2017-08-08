package com.ipati.dev.castleevent.model.Glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ipati.dev.castleevent.R

fun loadPhoto(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher)
    Glide.with(context).load(url).apply(requestOption).into(im)
}