package com.ipati.dev.castleevent.model.Glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions


class GlideConfig : AppGlideModule() {
    var decodeFormat: DecodeFormat = DecodeFormat.PREFER_ARGB_8888
    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        super.applyOptions(context, builder)
        val defaultRequestOption: RequestOptions = RequestOptions.formatOf(decodeFormat)
        builder?.setDefaultRequestOptions(defaultRequestOption)
    }

    override fun registerComponents(context: Context?, glide: Glide?, registry: Registry?) {
        super.registerComponents(context, glide, registry)
    }
}