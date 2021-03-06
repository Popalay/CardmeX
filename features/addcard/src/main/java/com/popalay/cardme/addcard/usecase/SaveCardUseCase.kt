package com.popalay.cardme.addcard.usecase

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.ui.navigation.Router
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class SaveCardUseCase(
    private val cardRepository: CardRepository,
    private val authRepository: AuthRepository,
    private val router: Router
) : UseCase<SaveCardUseCase.Action, SaveCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        val cardId = UUID.randomUUID().toString()
        val holderId = UUID.randomUUID().toString()

        authRepository.currentUser()
            .flatMapCompletable {
                val card = Card(
                    cardId,
                    action.number,
                    Holder(
                        holderId,
                        action.name.capitalize(),
                        ""
                    ),
                    action.isPublic,
                    action.cardType,
                    it.toNullable()?.uuid ?: "",
                    Date(),
                    Date()
                )
                cardRepository.save(card)
                    .doOnComplete { if (action.closeOnSuccess) router.navigateUp() }
            }
            .toSingleDefault(Result.Success)
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(
        val number: String,
        val name: String,
        val isPublic: Boolean,
        val cardType: CardType,
        val closeOnSuccess: Boolean
    ) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}