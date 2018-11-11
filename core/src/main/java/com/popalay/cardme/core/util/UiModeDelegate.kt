package com.popalay.cardme.core.util

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class UiModeDelegate(
    private val activity: AppCompatActivity,
    private val sharedPreferences: SharedPreferences
) {

    companion object {

        private const val KEY_UI_MODE = "KEY_UI_MODE"
    }

    fun applyDayNight() {
        val uiMode = getUiMode()
        AppCompatDelegate.setDefaultNightMode(uiMode)
    }

    fun toggleDayNight() {
        val uiMode = AppCompatDelegate.getDefaultNightMode().let { mode ->
            if (mode == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(uiMode)
        activity.recreate()
        persistUiMode(uiMode)
    }

    private fun persistUiMode(value: Int) {
        sharedPreferences.edit().putInt(KEY_UI_MODE, value).apply()
    }

    private fun getUiMode(): Int = sharedPreferences.getInt(KEY_UI_MODE, AppCompatDelegate.MODE_NIGHT_NO)
}