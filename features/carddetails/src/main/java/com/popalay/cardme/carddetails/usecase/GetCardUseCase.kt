package com.popalay.cardme.carddetails.usecase

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.CardRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class GetCardUseCase(
    private val cardRepository: CardRepository
) : UseCase<GetCardUseCase.Action, GetCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        cardRepository.get(action.cardId)
            .map { Result.Success(it.toNullable()) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val cardId: String) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val card: Card?) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}