package com.popalay.cardme.cardlist.usecase

import com.gojuno.koptional.None
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class CardListUseCase(
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : UseCase<CardListUseCase.Action, CardListUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        authRepository.authState()
            .switchMap { user -> user.toNullable()?.let { userRepository.get(it.uuid) } ?: Flowable.just(None) }
            .switchMap { user ->
                cardRepository.getAll(user.toNullable()?.uuid ?: "")
                    .map { cards -> cards.filter { it.id != user.toNullable()?.cardId } }
            }
            .map { if (it.isEmpty()) Result.Success(listOf(defaultCard)) else Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    private val defaultCard = Card(
        "0",
        "5555555555554444",
        Holder("0", "Glindev 4ever", ""),
        true,
        CardType.MASTER_CARD,
        "user_id",
        Date(),
        Date()
    )

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val cards: List<Card>) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}