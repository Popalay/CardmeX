package com.popalay.cardme.cardlist.usecase

import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.repository.CardRepository
import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class CardListUseCase(
    private val cardRepository: CardRepository
) : UseCase<CardListUseCase.Action, CardListUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        cardRepository.getAll()
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val cards: List<Card>) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}