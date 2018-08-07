package com.popalay.cardme.cache.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.popalay.cardme.cache.model.Card
import com.popalay.cardme.cache.model.CardWithHolder
import io.reactivex.Flowable

@Dao
internal abstract class CardDao : BaseDao<Card> {

    @Query("""SELECT * FROM Card ORDER BY updatedDate""")
    abstract fun findAll(): Flowable<List<Card>>

    @Query("""SELECT * FROM Card ORDER BY updatedDate""")
    abstract fun findAllWithHolder(): Flowable<List<CardWithHolder>>
}