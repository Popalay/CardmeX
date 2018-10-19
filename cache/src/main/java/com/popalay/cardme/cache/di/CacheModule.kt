package com.popalay.cardme.cache.di

import androidx.room.Room
import com.popalay.cardme.cache.dao.CacheCardDao
import com.popalay.cardme.cache.dao.CacheUserDao
import com.popalay.cardme.cache.database.Database
import com.popalay.cardme.cache.mapper.*
import org.koin.dsl.module.module

object CacheModule {

    fun get() = module {
        single {
            Room.databaseBuilder(get { it }, Database::class.java, "cardme-database")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<Database>().cardDao() }
        single { get<Database>().holderDao() }
        single { get<Database>().userDao() }
        single { CacheHolderToHolderMapper() }
        single { HolderToCacheHolderMapper() }
        single { CacheCardWithHolderToCardMapper(get { it }) }
        single { CardToCacheCardMapper() }
        single { UserToCacheUserMapper() }
        single { CacheUserToUserMapper() }
        single { CacheCardDao(get { it }, get { it }, get { it }, get { it }, get { it }) as com.popalay.cardme.api.cache.dao.CacheCardDao }
        single { CacheUserDao(get { it }, get { it }, get { it }) as com.popalay.cardme.api.cache.dao.CacheUserDao }
    }
}