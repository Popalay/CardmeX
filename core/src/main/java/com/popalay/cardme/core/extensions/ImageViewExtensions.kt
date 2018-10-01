package com.popalay.cardme.core.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

fun ImageView.loadImage(url: String?, @DrawableRes placeHolder: Int = 0, vararg transformation: Transformation) {
    Picasso.get()
        .load(url)
        .transform(transformation.toList())
        .apply { if (placeHolder != 0) placeholder(placeHolder) }
        .into(this)
}