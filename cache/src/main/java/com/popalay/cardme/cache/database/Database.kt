package com.popalay.cardme.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
    version = 2
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