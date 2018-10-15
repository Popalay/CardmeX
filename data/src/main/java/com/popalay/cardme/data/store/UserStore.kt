package com.popalay.cardme.data.store

import com.github.popalay.hoard.Hoard
import com.github.popalay.hoard.fetchpolicy.FetchPolicyFactory
import com.github.popalay.hoard.fetchpolicy.TimeFetchPolicy
import com.gojuno.koptional.Optional
import com.gojuno.koptional.Some
import com.popalay.cardme.api.cache.dao.CacheUserDao
import com.popalay.cardme.api.remote.dao.RemoteUserDao
import com.popalay.cardme.api.core.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class UserStore(
    cacheUserDao: CacheUserDao,
    remoteUserDao: RemoteUserDao
) : Hoard<Optional<User>, UserStore.Key>(
    fetcher = Fetcher(remoteUserDao),
    persister = Persister(cacheUserDao),
    fetchPolicy = FetchPolicyFactory.timeFetchPolicy(TimeFetchPolicy.MEDIUM_DELAY)
) {

    sealed class Key : com.github.popalay.hoard.Key {

        data class ById(val id: String) : Key()
    }

    private class Fetcher(
        private val remoteUserDao: RemoteUserDao
    ) : com.github.popalay.hoard.Fetcher<Optional<User>, Key> {

        override fun fetch(key: Key): Single<Optional<User>> = with(key) {
            when (this) {
                is Key.ById -> remoteUserDao.get(id)
                    .firstOrError()
            }
        }
    }

    private class Persister(
        private val cacheUserDao: CacheUserDao
    ) : com.github.popalay.hoard.Persister<Optional<User>, Key> {

        override fun read(key: Key): Flowable<Optional<User>> = with(key) {
            when (this) {
                is Key.ById -> cacheUserDao.get()
            }
        }

        override fun write(data: Optional<User>, key: Key): Completable =
            data.toNullable()?.let { cacheUserDao.save(it) } ?: Completable.complete()

        override fun isNotEmpty(key: Key): Single<Boolean> = with(key) {
            when (this) {
                is Key.ById -> cacheUserDao.isPresent()
            }
        }
    }
}