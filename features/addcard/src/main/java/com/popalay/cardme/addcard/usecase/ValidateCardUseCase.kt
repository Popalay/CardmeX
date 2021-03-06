package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.core.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class ValidateCardUseCase : UseCase<ValidateCardUseCase.Action, ValidateCardUseCase.Result> {

    private val masterCardRegex by lazy { Regex("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$") }
    private val visaRegex by lazy { Regex("^4[0-9]{12}(?:[0-9]{3})?$") }

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        Observable.fromCallable {
            action.name.isNotBlank() && action.number.isNotBlank() &&
                    (masterCardRegex.matches(action.number) || visaRegex.matches(action.number))
        }
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .startWith(Result.Idle(action.number, action.name, action.isPublic))
            .subscribeOn(Schedulers.io())
    }

    data class Action(val number: String, val name: String, val isPublic: Boolean) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val isValid: Boolean) : Result()
        data class Idle(val number: String, val name: String, val isPublic: Boolean) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}