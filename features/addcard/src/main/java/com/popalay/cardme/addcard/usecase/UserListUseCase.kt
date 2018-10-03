package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class UserListUseCase(
    private val userRepository: UserRepository
) : UseCase<UserListUseCase.Action, UserListUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        userRepository.getCurrentUser()
            .switchMap { userRepository.getAllLikeWithCard(action.query, action.lastDisplayName, action.limit) }
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val query: String, val lastDisplayName: String, val limit: Long) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val users: List<User>) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}