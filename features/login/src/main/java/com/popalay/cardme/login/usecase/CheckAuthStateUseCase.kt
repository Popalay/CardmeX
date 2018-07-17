package com.popalay.cardme.login.usecase

import com.google.firebase.auth.FirebaseAuth
import com.popalay.cardme.base.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CheckAuthStateUseCase : UseCase<CheckAuthStateUseCase.Action, CheckAuthStateUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        Single.just(FirebaseAuth.getInstance().currentUser != null)
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val state: Boolean) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}