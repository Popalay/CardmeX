package com.popalay.cardme.core.extensions

import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import io.reactivex.Single
import java.net.URL
import java.net.URLDecoder

fun Uri.createDynamicLink(): Single<String> = Single.create { emitter ->
    val builder = FirebaseDynamicLinks.getInstance()
        .createDynamicLink()
        .setLink(this)
        .setDomainUriPrefix("https://mecard.page.link/")
        .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())

    builder.buildShortDynamicLink()
        .addOnSuccessListener {
            val link = URL(URLDecoder.decode(it.shortLink.toString(), "UTF-8"))
            emitter.onSuccess(link.toString())
        }
        .addOnFailureListener {
            val link = URL(URLDecoder.decode(builder.buildDynamicLink().uri.toString(), "UTF-8"))
            emitter.onSuccess(link.toString())
        }
}