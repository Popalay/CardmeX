package com.popalay.cardme.cache.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.popalay.cardme.cache.converter.DateTypeConverter
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.dao.HolderDao
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheHolder

@Database(
    entities = [
        CacheCard::class,
        CacheHolder::class
    ],
    version = 1
)
@TypeConverters(
    value = [
        DateTypeConverter::class
    ]
)
internal abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao

    abstract fun holderDao(): HolderDao
}