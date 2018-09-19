package com.popalay.cardme.data.store

import com.github.popalay.hoard.Hoard
import com.github.popalay.hoard.fetchpolicy.FetchPolicyFactory
import com.github.popalay.hoard.fetchpolicy.TimeFetchPolicy
import com.popalay.cardme.api.cache.dao.CacheCardDao
import com.popalay.cardme.api.remote.dao.RemoteCardDao
import com.popalay.cardme.api.core.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class CardListStore(
    cacheCardDao: CacheCardDao,
    remoteCardDao: RemoteCardDao
) : Hoard<List<Card>, CardListStore.Key>(
    fetcher = Fetcher(remoteCardDao),
    persister = Persister(cacheCardDao),
    fetchPolicy = FetchPolicyFactory.timeFetchPolicy(TimeFetchPolicy.MEDIUM_DELAY)
) {

    sealed class Key : com.github.popalay.hoard.Key {

        data class AllByUser(val userId:String) : Key()
    }

    private class Fetcher(
        private val remoteCardDao: RemoteCardDao
    ) : com.github.popalay.hoard.Fetcher<List<Card>, Key> {

        override fun fetch(key: Key): Single<List<Card>> = with(key) {
            when (this) {
                is Key.AllByUser -> remoteCardDao.getAll(userId)
                    .first(listOf())
            }
        }
    }

    private class Persister(
        private val cacheCardDao: CacheCardDao
    ) : com.github.popalay.hoard.Persister<List<Card>, Key> {

        override fun read(key: Key): Flowable<List<Card>> = with(key) {
            when (this) {
                is Key.AllByUser -> cacheCardDao.getAll()
            }
        }

        override fun write(data: List<Card>, key: Key): Completable = cacheCardDao.saveAll(data)

        override fun isNotEmpty(key: Key): Single<Boolean> = with(key) {
            when (this) {
                is Key.AllByUser -> cacheCardDao.isNotEmpty()
            }
        }
    }
}