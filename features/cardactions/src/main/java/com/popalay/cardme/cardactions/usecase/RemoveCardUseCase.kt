package com.popalay.cardme.cardactions.usecase

import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.core.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class RemoveCardUseCase(
    private val cardRepository: CardRepository
) : UseCase<RemoveCardUseCase.Action, RemoveCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        cardRepository.delete(action.cardId)
            .toSingleDefault(Result.Success)
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val cardId: String) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}