package com.popalay.cardme.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.popalay.cardme.cache.converter.DateTypeConverter
import com.popalay.cardme.cache.database.dao.CardDao
import com.popalay.cardme.cache.database.dao.HolderDao
import com.popalay.cardme.cache.database.dao.UserDao
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheHolder
import com.popalay.cardme.cache.model.CacheUser

@Database(
    entities = [
        CacheCard::class,
        CacheHolder::class,
        CacheUser::class
    ],
    version = 3
)
@TypeConverters(
    value = [
        DateTypeConverter::class
    ]
)
internal abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao

    abstract fun holderDao(): HolderDao

    abstract fun userDao(): UserDao
}