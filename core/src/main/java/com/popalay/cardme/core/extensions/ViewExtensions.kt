package com.popalay.cardme.core.extensions

import android.widget.EditText

fun EditText.setTextIfNeeded(value: String?) {
    if (text.length != value?.length || text.toString() != value) {
        text.replace(0, text.length, value)
    }
}