package com.popalay.cardme.login.usecase

import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern

class ValidatePhoneNumberUseCase : UseCase<ValidatePhoneNumberUseCase.Action, ValidatePhoneNumberUseCase.Result> {

    private val phoneRegex = Pattern.compile("\\+?[0-9]{12}")

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        Single.just(phoneRegex.matcher(it.phoneNumber).matches())
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle(it.phoneNumber))
            .subscribeOn(Schedulers.io())
    }

    data class Action(val phoneNumber: String) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val valid: Boolean) : Result()
        data class Idle(val phoneNumber: String) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}