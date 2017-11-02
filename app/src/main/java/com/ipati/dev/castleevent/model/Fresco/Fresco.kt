package com.ipati.dev.castleevent.model.Fresco

import android.content.Context
import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.ipati.dev.castleevent.extension.matrixHeightPx
import com.ipati.dev.castleevent.extension.matrixWidthPx


fun loadPhoto(context: Context, url: String, widthCardView: Int, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(widthCardView)
    im.layoutParams.height = context.matrixHeightPx(350)

    val mImageRequest: ImageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(url))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val mDrawerController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(mImageRequest)
            .setOldController(im.controller)
            .build()

    im.controller = mDrawerController
    im.setImageURI(url, context)
}

fun loadLogo(context: Context, url: String, im: SimpleDraweeView) {
    im.layoutParams.height = context.matrixWidthPx(180)
    im.layoutParams.width = context.matrixHeightPx(180)

    val imageRequest: ImageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(url))
            .setResizeOptions(ResizeOptions(150, 150))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val mDrawerController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()

    im.controller = mDrawerController
    im.setImageURI(url, context)
}


fun loadPhotoDetail(context: Context, url: String, im: SimpleDraweeView) {
    val imageRequest: ImageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(url))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val mDrawerController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()
    im.controller = mDrawerController
    im.setImageURI(Uri.parse(url), context)
}

fun loadPhotoAdvertise(context: Context, url: String, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(context.resources.displayMetrics.widthPixels)
    im.layoutParams.height = context.matrixHeightPx(450)

    val imageRequest: ImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()
    im.controller = draweeController
    im.setImageURI(url, context)
}

fun loadPhotoProfileUser(context: Context, url: String?, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(360)
    im.layoutParams.height = context.matrixHeightPx(360)

    val imageRequest: ImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()

    im.controller = draweeController
    im.setImageURI(url, context)
}

fun loadPhotoItemMenu(context: Context, resource: Int, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(80)
    im.layoutParams.height = context.matrixHeightPx(80)

    val imageRequest: ImageRequest = ImageRequestBuilder.newBuilderWithResourceId(resource)
            .setLocalThumbnailPreviewsEnabled(true)
            .build()
    val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()

    im.controller = draweeController
    im.setActualImageResource(resource, context)

}

fun loadPhotoTickets(context: Context, url: String, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(140)
    im.layoutParams.height = context.matrixHeightPx(140)

    val imageRequest: ImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()
    val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()

    im.controller = draweeController
    im.setImageURI(url, context)
}

fun loadPhotoUserTickets(context: Context, url: String, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(150)
    im.layoutParams.height = context.matrixHeightPx(150)

    val imageRequest: ImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()
    im.controller = draweeController
    im.setImageURI(url, context)

}

fun loadPhotoUserProfile(context: Context, url: String, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(360)
    im.layoutParams.height = context.matrixHeightPx(360)

    val imageRequest: ImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()

    im.controller = draweeController
    im.setImageURI(url, context)
}

fun loadGoogleMapStatic(context: Context, latitude: Double, longitude: Double, im: SimpleDraweeView) {
    im.layoutParams.width = context.matrixWidthPx(context.resources.displayMetrics.widthPixels)
    im.layoutParams.height = context.matrixHeightPx(350)

    val queryMapStatic = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=$latitude,$longitude&zoom=14&size=500x200&markers=color:red%7Clabel:C%7C$latitude,$longitude" +
            "&key=AIzaSyBLaTYxYbFsvRkJg3_ayOjZ8-v2kN4qb9o"


    val imageRequest: ImageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(queryMapStatic))
            .setLocalThumbnailPreviewsEnabled(true)
            .build()

    val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(im.controller)
            .build()

    im.controller = draweeController
    im.setImageURI(queryMapStatic, context)
}
