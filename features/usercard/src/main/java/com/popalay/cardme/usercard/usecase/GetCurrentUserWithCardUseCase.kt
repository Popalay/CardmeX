package com.popalay.cardme.usercard.usecase

import com.gojuno.koptional.None
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class GetCurrentUserWithCardUseCase(
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : UseCase<GetCurrentUserWithCardUseCase.Action, GetCurrentUserWithCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        authRepository.authState()
            .switchMap { user -> user.toNullable()?.let { userRepository.get(it.uuid) } ?: Flowable.just(None) }
            .switchMap { optional ->
                val user = requireNotNull(optional.toNullable())
                cardRepository.get(user.cardId)
                    .map { Result.Success(user, it.toNullable()) }
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