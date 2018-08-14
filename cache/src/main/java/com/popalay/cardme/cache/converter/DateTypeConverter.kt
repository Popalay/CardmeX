package com.popalay.cardme.cache.converter

import androidx.room.TypeConverter
import java.util.Date

internal class DateTypeConverter {

    @TypeConverter fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter fun toLong(value: Date?): Long? = value?.time
}
