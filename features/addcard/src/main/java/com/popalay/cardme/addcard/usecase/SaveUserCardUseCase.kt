package com.popalay.cardme.addcard.usecase

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.UserRepository
import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.Date
import java.util.UUID

internal class SaveUserCardUseCase(
    private val userRepository: UserRepository,
    private val user: Optional<User>
) : UseCase<SaveUserCardUseCase.Action, SaveUserCardUseCase.Result> {

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

        userRepository.updateUserCard(card)
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