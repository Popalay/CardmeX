package com.popalay.cardme.core.util

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.popalay.cardme.core.MainActivity


class UiModeDelegate(
    private val sharedPreferences: SharedPreferences
) {

    companion object {

        private const val KEY_UI_MODE = "KEY_UI_MODE"
    }

    init {
        applyDayNight()
    }

    fun applyDayNight() {
        val uiMode = getUiMode()
        AppCompatDelegate.setDefaultNightMode(uiMode)
    }

    fun toggleDayNight(activity: FragmentActivity) {
        val uiMode = AppCompatDelegate.getDefaultNightMode().let { mode ->
            if (mode == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(uiMode)
        restartActivity(activity)
        persistUiMode(uiMode)
    }

    private fun persistUiMode(value: Int) {
        sharedPreferences.edit().putInt(KEY_UI_MODE, value).apply()
    }

    private fun getUiMode(): Int = sharedPreferences.getInt(KEY_UI_MODE, AppCompatDelegate.MODE_NIGHT_NO)

    private fun restartActivity(activity: FragmentActivity) {
        (activity as? MainActivity)?.restart()
    }
}