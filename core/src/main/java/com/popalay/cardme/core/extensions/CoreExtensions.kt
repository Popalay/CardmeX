package com.popalay.cardme.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

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