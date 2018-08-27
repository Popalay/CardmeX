package com.popalay.shaketoreport

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.popalay.shaketoreport.model.Config
import java.util.Date

class ShakeToReport(
    private val activity: AppCompatActivity,
    private val config: Config
) : LifecycleObserver {

    private val sensorManager: SensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val vibrationManager: Vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val vibrationPattern = longArrayOf(200, 200, 400)

    private val shakeDetector: ShakeDetector = ShakeDetector {
        runVibration()
        val lastReportTimeMillis = LastReportTimePersister.get(activity).time
        val reportTimeoutMillis = config.reportTimeout.timeUnit.toMillis(config.reportTimeout.time)
        val newReportDateMillis = lastReportTimeMillis + reportTimeoutMillis - Date().time
        if (newReportDateMillis <= 0) {
            showReportDialog()
        } else {
            Toast.makeText(activity, "You can create a new report in ${newReportDateMillis / 1000} seconds", Toast.LENGTH_LONG).show()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeDetection() {
        if (config.isEnabled) sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseDetection() {
        if (config.isEnabled) sensorManager.unregisterListener(shakeDetector)
    }

    private fun runVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrationManager.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1))
        } else {
            vibrationManager.vibrate(vibrationPattern, -1)
        }
    }

    private fun showReportDialog() {
        if (activity.supportFragmentManager.findFragmentByTag(ReportDialogFragment::class.java.simpleName) == null) {
            ReportDialogFragment().showNow(activity.supportFragmentManager, ReportDialogFragment::class.java.simpleName)
        }
    }
}