package com.popalay.cardme.cache.dao

import android.arch.persistence.room.Dao
import com.popalay.cardme.cache.model.CacheHolder

@Dao
internal abstract class HolderDao : BaseDao<CacheHolder>