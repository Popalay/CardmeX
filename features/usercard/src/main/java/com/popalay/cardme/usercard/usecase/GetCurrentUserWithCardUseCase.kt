package com.popalay.cardme.usercard.usecase

import com.gojuno.koptional.Some
import com.popalay.cardme.api.core.error.UnauthorizedException
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class GetCurrentUserWithCardUseCase(
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository
) : UseCase<GetCurrentUserWithCardUseCase.Action, GetCurrentUserWithCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        userRepository.getCurrentUser()
            .switchMap { user ->
                if (user is Some) cardRepository.get(user.value.cardId)
                    .map { Result.Success(user.value, it) }
                else Flowable.just(Result.Failure(UnauthorizedException()))
            }
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