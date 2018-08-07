package com.popalay.cardme.cache.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.model.Card
import com.popalay.cardme.cache.model.Holder

@Database(
    entities = [
        Card::class,
        Holder::class
    ],
    version = 1
)
internal abstract class Database : RoomDatabase() {

    abstract fun cardDao(): CardDao
}