package com.popalay.cardme.login.usecase

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.usecase.UseCase
import com.popalay.cardme.login.auth.CardmeAuthCredentials
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class AuthUseCase(
    private val authenticator: Authenticator
) : UseCase<AuthUseCase.Action, AuthUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        authenticator.auth(action.authCredentials)
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val authCredentials: CardmeAuthCredentials) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val user: Optional<User>) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}