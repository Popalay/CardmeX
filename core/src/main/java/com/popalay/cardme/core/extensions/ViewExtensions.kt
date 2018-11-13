package com.popalay.cardme.core.extensions

import android.widget.EditText

fun EditText.setTextIfNeeded(value: String?) {
    if (value.isNullOrBlank()) {
        text = null
    } else if (text.length != value.length || text.toString() != value) {
        text.replaceRange(0 until text.length, value)
    }
}