package com.popalay.cardme.cache.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheCardWithHolder
import io.reactivex.Flowable

@Dao
internal abstract class CardDao : BaseDao<CacheCard> {

    @Query("""SELECT * FROM CacheCard ORDER BY updatedDate""")
    abstract fun findAll(): Flowable<List<CacheCard>>

    @Query("""SELECT * FROM CacheCard ORDER BY updatedDate""")
    abstract fun findAllWithHolder(): Flowable<List<CacheCardWithHolder>>
}