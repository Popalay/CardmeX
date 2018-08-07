package com.popalay.cardme.cache.dao

import android.arch.persistence.room.Dao
import com.popalay.cardme.cache.model.Holder

@Dao
internal abstract class HolderDao : BaseDao<Holder>