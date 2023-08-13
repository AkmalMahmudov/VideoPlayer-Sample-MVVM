package com.akmal.androidtasklessons.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.akmal.androidtasklessons.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadImage(data: String?, errorDrawable: Drawable? = null) {
    if (data == null) return

    Glide.with(context)
        .load(data)
        .diskCacheStrategy(DiskCacheStrategy.ALL) // for caching purposes
        .error(errorDrawable)
//        .placeholder(R.drawable.placeholder)
//        .centerInside()
        .into(this)
}