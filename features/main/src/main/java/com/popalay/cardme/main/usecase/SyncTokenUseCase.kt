package com.popalay.cardme.main.usecase

import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.TokenRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import org.koin.standalone.KoinComponent

internal class SyncTokenUseCase(
    private val tokenRepository: TokenRepository
) : UseCase<SyncTokenUseCase.Action, SyncTokenUseCase.Result>, KoinComponent {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        tokenRepository.syncToken()
            .toSingleDefault(Result.Success)
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