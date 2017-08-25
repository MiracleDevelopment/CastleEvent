package com.ipati.dev.castleevent.model.Glide

import android.content.Context
import android.media.Image
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
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

fun loadPhotoProfileUser(context: Context, url: String?, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher)
            .transform(CircleCrop()).override(350, 350)

    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoItemMenu(context: Context, resource: Int, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher).override(50, 50)
    Glide.with(context).load(resource).apply(requestOption).into(im)
}
