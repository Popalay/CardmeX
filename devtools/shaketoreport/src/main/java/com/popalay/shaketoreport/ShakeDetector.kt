package com.popalay.shaketoreport

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

internal class ShakeDetector(val onShake: () -> Unit) : SensorEventListener {

    companion object {

        private const val SHAKE_THRESHOLD_GRAVITY = 2.7F
        private const val SHAKE_SLOP_TIME_MS = 500
    }

    private var shakeTimestamp: Long = 0

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // ignore
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        // gForce will be close to 1 when there is no movement.
        val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis()
            if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) return
            shakeTimestamp = now
            onShake()
        }
    }
}