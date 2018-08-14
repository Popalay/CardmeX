package com.popalay.cardme.cache.dao

import androidx.room.Dao
import androidx.room.Query
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheCardWithHolder
import io.reactivex.Flowable

@Dao
internal abstract class CardDao : BaseDao<CacheCard> {

    @Query("""SELECT * FROM cards ORDER BY updatedDate DESC""")
    abstract fun findAll(): Flowable<List<CacheCard>>

    @Query(
        """SELECT cards.id, cards.number, cards.isPublic, cards.createdDate, cards.updatedDate,
            holders.id as holder_id, holders.name as holder_name
            FROM cards LEFT JOIN holders ON cards.holderId = holders.id
            ORDER BY updatedDate DESC"""
    )
    abstract fun findAllWithHolder(): Flowable<List<CacheCardWithHolder>>
}