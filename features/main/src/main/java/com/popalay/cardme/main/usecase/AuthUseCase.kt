package com.popalay.cardme.main.usecase

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.UserRepository
import com.popalay.cardme.api.usecase.UseCase
import com.popalay.cardme.main.auth.CardmeAuthCredentials
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.standalone.KoinComponent
import org.koin.standalone.release

internal class AuthUseCase(
    private val authenticator: Authenticator,
    private val userRepository: UserRepository
) : UseCase<AuthUseCase.Action, AuthUseCase.Result>, KoinComponent {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        authenticator.auth(action.authCredentials)
            .flatMap { user -> user.toNullable()?.let { userRepository.save(it).toSingleDefault(user) } ?: Single.just(user) }
            .map { Result.Success(it) }
            .doOnSuccess { release("") }
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