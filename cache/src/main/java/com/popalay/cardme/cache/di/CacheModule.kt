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
            Room.databaseBuilder(get(), Database::class.java, "cardme-database")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<Database>().cardDao() }
        single { get<Database>().holderDao() }
        single { get<Database>().userDao() }
        single { CacheHolderToHolderMapper() }
        single { HolderToCacheHolderMapper() }
        single { CacheCardWithHolderToCardMapper(get()) }
        single { CardToCacheCardMapper() }
        single { UserToCacheUserMapper(get()) }
        single { CacheUserToUserMapper(get()) }
        single { CacheCardDao(get(), get(), get(), get(), get()) as com.popalay.cardme.api.cache.dao.CacheCardDao }
        single { CacheUserDao(get(), get(), get()) as com.popalay.cardme.api.cache.dao.CacheUserDao }
    }
}