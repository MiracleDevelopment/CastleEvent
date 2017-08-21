package com.ipati.dev.castleevent.model.Glide

import android.content.Context
import android.media.Image
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ipati.dev.castleevent.R

fun loadPhoto(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher).override(200, 200)
    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadLogo(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher).override(150, 150)
    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoDetail(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher)
    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoAdvertise(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher)
    Glide.with(context).load(url).apply(requestOption).into(im)
}