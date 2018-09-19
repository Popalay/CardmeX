package com.popalay.cardme.cardlist.usecase

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.data.repository.UserRepository
import com.popalay.cardme.api.core.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class CardListUseCase(
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository
) : UseCase<CardListUseCase.Action, CardListUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        userRepository.getCurrentUser()
            .switchMap { cardRepository.getAll(it.toNullable()?.uuid ?: "") }
            .map { if (it.isEmpty()) Result.Success(listOf(defaultCard)) else Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    private val defaultCard: Card
        get() = Card(
            "0",
            "5555555555554444",
            Holder("0", "Glindev 4ever"),
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