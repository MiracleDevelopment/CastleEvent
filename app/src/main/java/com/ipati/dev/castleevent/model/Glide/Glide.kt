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
    val requestOption: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .override(200, 200)

    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadLogo(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .override(150, 150)

    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoDetail(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoAdvertise(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher)
    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoProfileUser(context: Context, url: String?, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .transform(CircleCrop())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(350, 350)

    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoItemMenu(context: Context, resource: Int, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .override(50, 50)
    Glide.with(context).load(resource).apply(requestOption).into(im)
}

fun loadPhotoTickets(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .override(140, 140)

    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoUserTickets(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher)
            .circleCrop()
            .override(120, 120)

    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadPhotoUserProfile(context: Context, url: String, im: ImageView) {
    val requestOption: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .override(350, 350)

    Glide.with(context).load(url).apply(requestOption).into(im)
}

fun loadGoogleMapStatic(context: Context, latitude: Double, longitude: Double, im: ImageView) {
    val queryMapStatic = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=$latitude,$longitude&zoom=14&size=500x200&markers=color:red%7Clabel:C%7C$latitude,$longitude" +
            "&key=AIzaSyBLaTYxYbFsvRkJg3_ayOjZ8-v2kN4qb9o"

    val requestOption: RequestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(context).load(queryMapStatic).apply(requestOption).into(im)
}
