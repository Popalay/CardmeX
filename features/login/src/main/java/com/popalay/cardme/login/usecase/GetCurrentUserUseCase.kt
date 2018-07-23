package com.popalay.cardme.login.usecase

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.firebase.auth.FirebaseAuth
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.usecase.UseCase
import com.popalay.cardme.login.toUser
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GetCurrentUserUseCase : UseCase<GetCurrentUserUseCase.Action, GetCurrentUserUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        Single.just<Optional<User>>(FirebaseAuth.getInstance().currentUser?.toUser().toOptional())
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val user: Optional<User>) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}