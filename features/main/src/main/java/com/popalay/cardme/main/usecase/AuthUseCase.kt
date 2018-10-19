package com.popalay.cardme.main.usecase

import com.popalay.cardme.api.auth.AuthCredentialsFactory
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.standalone.KoinComponent

internal class AuthUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val authCredentialsFactory: AuthCredentialsFactory
) : UseCase<AuthUseCase.Action, AuthUseCase.Result>, KoinComponent {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        authRepository.auth(authCredentialsFactory.build())
            .flatMap { user -> user.toNullable()?.let { userRepository.save(it).toSingleDefault(user) } ?: Single.just(user) }
            .map { Result.Success }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}