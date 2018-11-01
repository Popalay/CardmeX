package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Completable
import io.reactivex.Flowable

internal abstract class BaseRepository {

    protected fun <D : Any> flowElement(
        cacheFlow: Flowable<Optional<D>>,
        remoteFlow: Flowable<Optional<D>>,
        persist: (Optional<D>) -> Completable
    ): Flowable<Optional<D>> = flow(
        cacheFlow.map { listOfNotNull(it.toNullable()) },
        remoteFlow.map { listOfNotNull(it.toNullable()) }
    ) { list -> persist(list.firstOrNull().toOptional()) }
        .map { it.firstOrNull().toOptional() }

    protected fun <D : Any> flow(
        cacheFlow: Flowable<List<D>>,
        remoteFlow: Flowable<List<D>>,
        persist: (List<D>) -> Completable
    ): Flowable<List<D>> = with(cacheFlow.share()) {
        Flowable.merge(
            take(1).flatMap { handleRemoteFlow(remoteFlow, persist).startWith(it) },
            skip(1)
        ).distinctUntilChanged()
    }

    private fun <D> handleRemoteFlow(
        remoteFlow: Flowable<List<D>>,
        persist: (List<D>) -> Completable
    ): Flowable<List<D>> = remoteFlow
        .filter { it.isNotEmpty() }
        .doOnNext { persist(it).blockingAwait() }
        .filter { false }
}