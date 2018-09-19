package com.popalay.cardme.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.popalay.cardme.cache.model.CacheUser
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
internal abstract class UserDao : BaseDao<CacheUser> {

    @Query("DELETE FROM users")
    abstract fun deleteAll()

    @Query("""SELECT * FROM users LIMIT 1""")
    abstract fun findOne(): Flowable<List<CacheUser>>

    @Query("""SELECT count(*) FROM users LIMIT 1""")
    abstract fun isNotEmpty(): Single<Boolean>
}