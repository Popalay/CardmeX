package com.popalay.cardme.addcard.usecase

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.CardRepository
import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.Date
import java.util.UUID

internal class SaveCardUseCase(
    private val cardRepository: CardRepository,
    private val user: Optional<User>
) : UseCase<SaveCardUseCase.Action, SaveCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        val cardId = UUID.randomUUID().toString()
        val holderId = UUID.randomUUID().toString()
        val card = Card(
            cardId,
            it.number,
            Holder(holderId, it.name),
            it.isPublic,
            it.cardType,
            user.toNullable()?.uuid ?: "",
            Date(),
            Date()
        )

        cardRepository.save(card)
            .toSingleDefault(Result.Success)
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val number: String, val name: String, val isPublic: Boolean, val cardType: CardType) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}