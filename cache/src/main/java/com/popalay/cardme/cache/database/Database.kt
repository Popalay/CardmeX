package com.popalay.cardme.cache.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheHolder

@Database(
    entities = [
        CacheCard::class,
        CacheHolder::class
    ],
    version = 1
)
internal abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao
}