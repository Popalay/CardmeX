package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class SaveUserCardUseCase(
    private val userRepository: UserRepository
) : UseCase<SaveUserCardUseCase.Action, SaveUserCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        val cardId = UUID.randomUUID().toString()

        //TODO: save user card
        userRepository.getCurrentUser()
            .firstElement()
            .flatMapCompletable {
                val userId = it.toNullable()?.uuid ?: UUID.randomUUID().toString()

                val card = Card(
                    cardId,
                    action.number,
                    Holder(userId, action.name, it.toNullable()?.photoUrl ?: ""),
                    action.isPublic,
                    action.cardType,
                    userId,
                    Date(),
                    Date()
                )
                userRepository.update(requireNotNull(it.toNullable()))
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