package com.popalay.cardme.cache.di

import android.arch.persistence.room.Room
import com.popalay.cardme.api.data.DataSource
import com.popalay.cardme.api.data.Persister
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.cache.database.Database
import com.popalay.cardme.cache.datasource.CardCacheDataSource
import com.popalay.cardme.cache.mapper.CardMapper
import com.popalay.cardme.cache.mapper.HolderMapper
import com.popalay.cardme.cache.mapper.RevertCardMapper
import com.popalay.cardme.cache.mapper.RevertHolderMapper
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheCardWithHolder
import com.popalay.cardme.cache.model.CacheHolder
import com.popalay.cardme.cache.persister.CardCachePersister
import com.popalay.cardme.cache.persister.HolderCachePersister
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
        single { HolderMapper() as Mapper<CacheHolder, Holder> }
        single { RevertHolderMapper() as Mapper<Holder, CacheHolder> }
        single { CardMapper(get()) as Mapper<CacheCardWithHolder, Card> }
        single { RevertCardMapper() as Mapper<Card, CacheCard> }
        single { CardCacheDataSource(get(), get()) as DataSource<List<Card>, Source.Cache> }
        single { CardCachePersister(get(), get()) as Persister<Card, Source.Cache> }
        single { HolderCachePersister(get(), get()) as Persister<Holder, Source.Cache> }
    }
}