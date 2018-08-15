package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class IdentifyCardNumberUseCase : UseCase<IdentifyCardNumberUseCase.Action, IdentifyCardNumberUseCase.Result> {

    private val masterCardRegex by lazy { Regex("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$") }
    private val visaRegex by lazy { Regex("^4[0-9]{12}(?:[0-9]{3})?$") }

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        Observable.fromCallable {
            when {
                masterCardRegex.matches(action.number) -> CardType.MASTER_CARD
                visaRegex.matches(action.number) -> CardType.VISA
                else -> CardType.UNKNOWN
            }
        }
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val number: String) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val cardType: CardType) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}