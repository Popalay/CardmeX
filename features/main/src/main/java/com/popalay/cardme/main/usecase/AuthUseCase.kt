package com.popalay.cardme.main.usecase

import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.data.repository.UserRepository
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.authenticator.CardmeAuthCredentials
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.standalone.KoinComponent

internal class AuthUseCase(
    private val authenticator: Authenticator,
    private val userRepository: UserRepository
) : UseCase<AuthUseCase.Action, AuthUseCase.Result>, KoinComponent {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        authenticator.auth(action.authCredentials)
            .flatMap { user -> user.toNullable()?.let { userRepository.save(it).toSingleDefault(user) } ?: Single.just(user) }
            .map { Result.Success(it.toNullable()) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val authCredentials: CardmeAuthCredentials) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val user: User?) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}