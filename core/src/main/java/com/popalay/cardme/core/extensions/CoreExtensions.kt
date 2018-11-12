package com.popalay.cardme.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.fragment.app.FragmentActivity

fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (ex: Exception) {
        null
    }
}

@SuppressLint("MissingPermission")
fun Context.vibrate(vibrationPattern: LongArray) {
    val vibrationManager = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrationManager.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1))
    } else {
        @Suppress("DEPRECATION")
        vibrationManager.vibrate(vibrationPattern, -1)
    }
}

fun FragmentActivity.fixStatusBarColor() {
    val a = obtainStyledAttributes(intArrayOf(android.R.attr.windowLightStatusBar))
    val windowLightStatusBar = a.getBoolean(0, false)
    a.recycle()
    val flag = window.decorView.systemUiVisibility.let {
        if (windowLightStatusBar) {
            it or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            it and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
    window.decorView.systemUiVisibility = flag
}