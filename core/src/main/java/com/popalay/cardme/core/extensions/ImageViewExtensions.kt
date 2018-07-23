package com.popalay.cardme.core.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

fun ImageView.loadImage(url: String, vararg transformation: Transformation) {
    Picasso.get()
        .load(url)
        .transform(transformation.toList())
        .into(this)
}