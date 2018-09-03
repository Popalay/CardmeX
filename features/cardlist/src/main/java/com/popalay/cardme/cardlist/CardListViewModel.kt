package com.popalay.cardme.cardlist

import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.cardlist.usecase.CardListUseCase
import com.popalay.cardme.cardlist.usecase.CopyCardNumberUseCase
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import io.reactivex.rxkotlin.ofType

internal class CardListViewModel(
    private val cardListUseCase: CardListUseCase,
    private val specificIntentUseCase: SpecificIntentUseCase,
    private val copyCardNumberUseCase: CopyCardNumberUseCase
) : BaseMviViewModel<CardListViewState, CardListIntent>() {

    override val initialState: CardListViewState = CardListViewState()

    override val processor: Processor<CardListIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<CardListIntent.OnStart>()
                .map { CardListUseCase.Action }
                .compose(cardListUseCase),
            observable.ofType<CardListIntent.OnAddCardClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<CardListIntent.OnAddCardDialogDismissed>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<CardListIntent.OnCardActionsDialogDismissed>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<CardListIntent.OnCardClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<CardListIntent.OnCardLongClicked>()
                .map { CopyCardNumberUseCase.Action(it.card) }
                .compose(copyCardNumberUseCase)
        )
    }

    override val reducer: Reducer<CardListViewState> = LambdaReducer {
        when (this) {
            is CardListUseCase.Result -> when (this) {
                is CardListUseCase.Result.Success -> it.copy(cards = cards, progress = false)
                CardListUseCase.Result.Idle -> it.copy(progress = true)
                is CardListUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is CopyCardNumberUseCase.Result -> when (this) {
                is CopyCardNumberUseCase.Result.Success -> it.copy(toastMessage = "The card in the clipboard", showToast = true)
                CopyCardNumberUseCase.Result.Idle -> it
                is CopyCardNumberUseCase.Result.Failure -> it.copy(error = throwable)
                CopyCardNumberUseCase.Result.HideMessage -> it.copy(toastMessage = null, showToast = false)
            }
            is SpecificIntentUseCase.Result -> with(intent as CardListIntent) {
                when (this) {
                    CardListIntent.OnAddCardClicked -> it.copy(showAddCardDialog = true)
                    CardListIntent.OnAddCardDialogDismissed -> it.copy(showAddCardDialog = false)
                    CardListIntent.OnCardActionsDialogDismissed -> it.copy(selectedCard = null)
                    is CardListIntent.OnCardClicked -> it.copy(selectedCard = card)
                    else -> throw UnsupportedOperationException()
                }
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}