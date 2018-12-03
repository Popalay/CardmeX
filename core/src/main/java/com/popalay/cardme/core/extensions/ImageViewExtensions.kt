package com.popalay.cardme.core.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.popalay.cardme.R
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.core.picasso.CircleBorderedImageTransformation
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.widget.TextDrawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.Transformation

class Placeholder private constructor(
    @DrawableRes val placeHolderRes: Int = 0,
    val placeHolderDrawable: Drawable? = null
) {

    constructor(@DrawableRes placeHolderRes: Int) : this(placeHolderRes, null)

    constructor(placeHolderDrawable: Drawable?) : this(0, placeHolderDrawable)
}

fun RequestCreator.placeholder(placeHolder: Placeholder): RequestCreator = apply {
    if (placeHolder.placeHolderRes != 0) {
        error(placeHolder.placeHolderRes)
        placeholder(placeHolder.placeHolderRes)
    } else if (placeHolder.placeHolderDrawable != null) {
        error(placeHolder.placeHolderDrawable)
        placeholder(placeHolder.placeHolderDrawable)
    }
}

fun ImageView.setFromPlaceholder(placeHolder: Placeholder) {
    if (placeHolder.placeHolderRes != 0) {
        setImageResource(placeHolder.placeHolderRes)
    } else if (placeHolder.placeHolderDrawable != null) {
        setImageDrawable(placeHolder.placeHolderDrawable)
    }
}

fun ImageView.loadImage(url: String?, placeHolder: Placeholder, vararg transformation: Transformation) {
    Picasso.get()
        .load(url?.takeIf { it.isNotBlank() })
        .transform(transformation.toList())
        .placeholder(placeHolder)
        .apply { if (url.isNullOrBlank()) setFromPlaceholder(placeHolder) }
        .into(this)
}

fun ImageView.showMenuUser(value: User) {
    val placeholder = value.displayName.value.toTextDrawable(context)
    loadImage(
        value.photoUrl,
        Placeholder(placeholder),
        CircleBorderedImageTransformation(2.px.toFloat(), ContextCompat.getColor(context, R.color.secondary))
    )
}

fun ImageView.showUser(value: User) {
    val placeholder = value.displayName.value.toTextDrawable(context)
    loadImage(
        value.photoUrl,
        Placeholder(placeholder),
        CircleImageTransformation()
    )
}

fun ImageView.showHolder(value: Holder) {
    val placeholder = value.name.toTextDrawable(context)
    loadImage(value.photoUrl, Placeholder(placeholder), CircleImageTransformation())
}

private fun String.toTextDrawable(context: Context) = TextDrawable(context, getOrElse(0) { ' ' }.toString())