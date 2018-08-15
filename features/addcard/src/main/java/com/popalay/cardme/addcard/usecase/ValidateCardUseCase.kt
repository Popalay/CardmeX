package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class ValidateCardUseCase : UseCase<ValidateCardUseCase.Action, ValidateCardUseCase.Result> {

    private val cardNumberRegex by lazy { Regex("\\b\\d{13,16}\\b") }

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        Observable.fromCallable {
            action.name.isNotBlank() && action.number.isNotBlank() && cardNumberRegex.matches(action.number)
        }
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val number: String, val name: String, val isPublic: Boolean) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val isValid: Boolean) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}