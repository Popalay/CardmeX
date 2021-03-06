package com.popalay.cardme.cache.dao

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.popalay.cardme.api.cache.dao.CacheCardDao
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.cache.database.dao.CardDao
import com.popalay.cardme.cache.database.dao.HolderDao
import com.popalay.cardme.cache.mapper.CacheCardWithHolderToCardMapper
import com.popalay.cardme.cache.mapper.CardToCacheCardMapper
import com.popalay.cardme.cache.mapper.HolderToCacheHolderMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class CacheCardDao(
    private val cardDao: CardDao,
    private val cardMapper: CardToCacheCardMapper,
    private val holderMapper: HolderToCacheHolderMapper,
    private val holderDao: HolderDao,
    private val cacheCardMapper: CacheCardWithHolderToCardMapper
) : CacheCardDao {

    override fun save(data: Optional<Card>): Completable = Completable.fromAction {
        data.toNullable()?.let {
            holderDao.insertOrUpdate(holderMapper(it.holder))
            cardDao.insertOrUpdate(cardMapper(it))
        }
    }.subscribeOn(Schedulers.io())

    override fun saveAll(data: List<Card>): Completable = Completable.fromAction {
        holderDao.insertAll(data.map { holderMapper(it.holder) })
        cardDao.insertAll(data.map { cardMapper(it) })
    }.subscribeOn(Schedulers.io())

    override fun get(id: String): Flowable<Optional<Card>> = cardDao.findOneWithHolder(id)
        .map { cards -> cards.firstOrNull()?.let { cacheCardMapper(it) }.toOptional() }
        .subscribeOn(Schedulers.io())

    override fun getAll(): Flowable<List<Card>> = cardDao.findAllWithHolder()
        .map { it.map(cacheCardMapper::invoke) }
        .subscribeOn(Schedulers.io())

    override fun delete(id: String): Completable = Completable.fromAction {
        cardDao.delete(id)
    }.subscribeOn(Schedulers.io())

    override fun isPresent(id: String): Single<Boolean> = cardDao.isPresent(id)
        .subscribeOn(Schedulers.io())

    override fun isNotEmpty(): Single<Boolean> = cardDao.isNotEmpty()
        .subscribeOn(Schedulers.io())
}