package com.popalay.cardme.data.store

import com.github.popalay.hoard.Hoard
import com.github.popalay.hoard.fetchpolicy.FetchPolicyFactory
import com.github.popalay.hoard.fetchpolicy.TimeFetchPolicy
import com.popalay.cardme.api.dao.CacheCardDao
import com.popalay.cardme.api.dao.RemoteCardDao
import com.popalay.cardme.api.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class CardStore(
    cacheCardDao: CacheCardDao,
    remoteCardDao: RemoteCardDao
) : Hoard<Card, CardStore.Key>(
    fetcher = Fetcher(remoteCardDao),
    persister = Persister(cacheCardDao),
    fetchPolicy = FetchPolicyFactory.timeFetchPolicy(TimeFetchPolicy.MEDIUM_DELAY)
) {

    sealed class Key : com.github.popalay.hoard.Key {

        data class ById(val id: String) : Key()
    }

    private class Fetcher(
        private val remoteCardDao: RemoteCardDao
    ) : com.github.popalay.hoard.Fetcher<Card, Key> {

        override fun fetch(key: Key): Single<Card> = with(key) {
            when (this) {
                is Key.ById -> remoteCardDao.get(id)
                    .firstOrError()
            }
        }
    }

    private class Persister(
        private val cacheCardDao: CacheCardDao
    ) : com.github.popalay.hoard.Persister<Card, Key> {

        override fun read(key: Key): Flowable<Card> = with(key) {
            when (this) {
                is Key.ById -> cacheCardDao.get(id)
            }
        }

        override fun write(data: Card, key: Key): Completable = cacheCardDao.save(data)

        override fun isNotEmpty(key: Key): Single<Boolean> = with(key) {
            when (this) {
                is Key.ById -> cacheCardDao.isNotEmpty()
            }
        }
    }
}