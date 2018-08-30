package com.popalay.cardme.usercard.usecase

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.UserRepository
import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class GetUserCardUseCase(
    private val user: Optional<User>,
    private val userRepository: UserRepository
) : UseCase<GetUserCardUseCase.Action, GetUserCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        userRepository.getUserCard()
            .map { Result.Success(requireNotNull(user.toNullable()), it.toNullable()) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val user: User, val card: Card?) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}