package com.popalay.shaketoreport

import android.content.Context
import android.preference.PreferenceManager
import java.util.Date

object LastReportTimePersister {

    private const val KEY_LAST_REPORT_AT = "KEY_LAST_REPORT_AT"

    fun save(context: Context, date: Date) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putLong(KEY_LAST_REPORT_AT, date.time)
            .apply()
    }

    fun get(context: Context): Date =
        Date(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(KEY_LAST_REPORT_AT, 0)
        )
}