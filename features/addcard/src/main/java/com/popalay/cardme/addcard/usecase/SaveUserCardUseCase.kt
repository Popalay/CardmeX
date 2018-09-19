package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.api.repository.UserRepository
import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class SaveUserCardUseCase(
    private val userRepository: UserRepository
) : UseCase<SaveUserCardUseCase.Action, SaveUserCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        val cardId = UUID.randomUUID().toString()
        val holderId = UUID.randomUUID().toString()

        userRepository.getCurrentUser()
            .flatMapCompletable {
                val card = Card(
                    cardId,
                    action.number,
                    Holder(holderId, action.name),
                    action.isPublic,
                    action.cardType,
                    it.toNullable()?.uuid ?: "",
                    Date(),
                    Date()
                )
                userRepository.update(requireNotNull(it.toNullable()).copy(card = card))
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