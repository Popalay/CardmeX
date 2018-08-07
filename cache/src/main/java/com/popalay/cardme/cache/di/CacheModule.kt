package com.popalay.cardme.cache.di

import android.arch.persistence.room.Room
import com.popalay.cardme.cache.database.Database
import com.popalay.cardme.cache.datasource.CardCacheDataSource
import com.popalay.cardme.cache.mapper.CardMapper
import com.popalay.cardme.cache.mapper.HolderMapper
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object CacheModule {

    fun get() = module(CacheModule::class.moduleName) {
        single {
            Room.databaseBuilder(get(), Database::class.java, "cardme-database")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<Database>().cardDao() }
        single { HolderMapper() }
        single { CardMapper(get()) }
        single { CardCacheDataSource(get(), get()) }
    }
}