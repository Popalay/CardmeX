package com.popalay.cardme.core.usecase

import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) : UseCase<GetCurrentUserUseCase.Action, GetCurrentUserUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        authRepository.authState()
            .map { Result.Success(it.toNullable()) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val user: User?) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}