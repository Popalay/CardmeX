package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class SaveUserCardUseCase(
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository
) : UseCase<SaveUserCardUseCase.Action, SaveUserCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        userRepository.getCurrentUser()
            .firstElement()
            .flatMapCompletable { optional ->
                val user = requireNotNull(optional.toNullable())
                val cardId = user.cardId.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()

                val card = Card(
                    cardId,
                    action.number,
                    Holder(user.uuid, action.name, user.photoUrl),
                    action.isPublic,
                    action.cardType,
                    user.uuid,
                    Date(),
                    Date()
                )
                Completable.mergeArray(
                    cardRepository.save(card),
                    userRepository.update(requireNotNull(optional.toNullable()).copy(cardId = cardId))
                )
            }
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