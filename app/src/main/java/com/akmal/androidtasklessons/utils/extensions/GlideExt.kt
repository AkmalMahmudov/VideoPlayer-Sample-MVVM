package com.akmal.androidtasklessons.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadImage(data: String?, corners: Int? = null, errorDrawable: Drawable? = null) {
    if (data == null) return

    Glide.with(context)
        .load(data)
//        .transform(RoundedCorners((corners ?: 1)))
        .diskCacheStrategy(DiskCacheStrategy.ALL) // for caching purposes
        .error(errorDrawable)
        .into(this)
}