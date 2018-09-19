package com.popalay.cardme.cache.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.popalay.cardme.cache.model.CacheHolder

@Dao
internal abstract class HolderDao : BaseDao<CacheHolder> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(holders: List<CacheHolder>)
}