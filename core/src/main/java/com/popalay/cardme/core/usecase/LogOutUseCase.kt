package com.popalay.cardme.core.usecase

import com.google.firebase.auth.FirebaseAuth
import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class LogOutUseCase : UseCase<LogOutUseCase.Action, LogOutUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        Completable.fromAction { FirebaseAuth.getInstance().signOut() }
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