package com.popalay.cardme.data.store

import com.github.popalay.hoard.Hoard
import com.github.popalay.hoard.fetchpolicy.FetchPolicyFactory
import com.github.popalay.hoard.fetchpolicy.TimeFetchPolicy
import com.gojuno.koptional.Optional
import com.popalay.cardme.api.cache.dao.CacheCardDao
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.remote.dao.RemoteCardDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class CardStore(
    cacheCardDao: CacheCardDao,
    remoteCardDao: RemoteCardDao
) : Hoard<Optional<Card>, CardStore.Key>(
    fetcher = Fetcher(remoteCardDao),
    persister = Persister(cacheCardDao),
    fetchPolicy = FetchPolicyFactory.timeFetchPolicy(TimeFetchPolicy.MEDIUM_DELAY)
) {

    sealed class Key : com.github.popalay.hoard.Key {

        data class ById(val id: String) : Key()
    }

    private class Fetcher(
        private val remoteCardDao: RemoteCardDao
    ) : com.github.popalay.hoard.Fetcher<Optional<Card>, Key> {

        override fun fetch(key: Key): Single<Optional<Card>> = with(key) {
            when (this) {
                is Key.ById -> remoteCardDao.get(id)
                    .firstOrError()
            }
        }
    }

    private class Persister(
        private val cacheCardDao: CacheCardDao
    ) : com.github.popalay.hoard.Persister<Optional<Card>, Key> {

        override fun read(key: Key): Flowable<Optional<Card>> = with(key) {
            when (this) {
                is Key.ById -> cacheCardDao.get(id)
            }
        }

        override fun write(data: Optional<Card>, key: Key): Completable =
            data.toNullable()?.let { cacheCardDao.save(it) } ?: Completable.complete()

        override fun isNotEmpty(key: Key): Single<Boolean> = with(key) {
            when (this) {
                is Key.ById -> cacheCardDao.isPresent(id)
            }
        }
    }
}