package com.popalay.cardme.cache.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheCardWithHolder
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
internal abstract class CardDao : BaseDao<CacheCard> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(cards: List<CacheCard>)

    @Query("DELETE FROM cards WHERE cards.id = :cardId")
    abstract fun delete(cardId: String)

    @Query("""SELECT * FROM cards ORDER BY updatedDate DESC""")
    abstract fun findAll(): Flowable<List<CacheCard>>

    @Query(
            """SELECT cards.*, holders.id as holder_id, holders.name as holder_name
            FROM cards LEFT JOIN holders ON cards.holderId = holders.id
            ORDER BY updatedDate DESC"""
    )
    abstract fun findAllWithHolder(): Flowable<List<CacheCardWithHolder>>

    @Query(
            """SELECT cards.*, holders.id as holder_id, holders.name as holder_name
            FROM cards LEFT JOIN holders ON cards.holderId = holders.id
            WHERE cards.id = :cardId
            ORDER BY updatedDate DESC LIMIT 1"""
    )
    abstract fun findOneWithHolder(cardId: String): Flowable<CacheCardWithHolder>

    @Query("""SELECT count(id) FROM cards WHERE id = :id LIMIT 1""")
    abstract fun isPresent(id: String): Single<Boolean>

    @Query("""SELECT count(*) FROM cards LIMIT 1""")
    abstract fun isNotEmpty(): Single<Boolean>
}