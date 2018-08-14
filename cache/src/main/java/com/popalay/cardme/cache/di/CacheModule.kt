package com.popalay.cardme.cache.di

import androidx.room.Room
import com.popalay.cardme.cache.database.Database
import com.popalay.cardme.cache.datasource.CardCacheDataSource
import com.popalay.cardme.cache.mapper.CacheCardWithHolderToCardMapper
import com.popalay.cardme.cache.mapper.CacheHolderToHolderMapper
import com.popalay.cardme.cache.mapper.CardToCacheCardMapper
import com.popalay.cardme.cache.mapper.HolderToCacheHolderMapper
import com.popalay.cardme.cache.persister.CardCachePersister
import com.popalay.cardme.cache.persister.HolderCachePersister
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
        single { CacheHolderToHolderMapper() }
        single { HolderToCacheHolderMapper() }
        single { CacheCardWithHolderToCardMapper(get()) }
        single { CardToCacheCardMapper() }
        single { CardCacheDataSource(get(), get()) as com.popalay.cardme.api.data.datasource.CardCacheDataSource }
        single { CardCachePersister(get(), get(), get(), get()) as com.popalay.cardme.api.data.persister.CardCachePersister }
        single { HolderCachePersister(get(), get()) as com.popalay.cardme.api.data.persister.HolderCachePersister }
    }
}