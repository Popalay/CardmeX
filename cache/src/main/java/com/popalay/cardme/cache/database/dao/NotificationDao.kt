package com.popalay.cardme.cache.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.popalay.cardme.cache.model.CacheNotification
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
internal abstract class NotificationDao : BaseDao<CacheNotification> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(cards: List<CacheNotification>): Completable

    @Query("DELETE FROM notification WHERE id = :id")
    abstract fun delete(id: String)

    @Query("""SELECT * FROM notification WHERE toUserUuid = :userId ORDER BY createdDate DESC""")
    abstract fun findAll(userId: String): Flowable<List<CacheNotification>>
}