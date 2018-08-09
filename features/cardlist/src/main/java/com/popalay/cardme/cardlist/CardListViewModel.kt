package com.popalay.cardme.cardlist

import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.cardlist.usecase.CardListUseCase
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import io.reactivex.rxkotlin.ofType

internal class CardListViewModel(
    private val cardListUseCase: CardListUseCase,
    private val specificIntentUseCase: SpecificIntentUseCase
) : BaseMviViewModel<CardListViewState, CardListIntent>() {

    override val initialState: CardListViewState = CardListViewState.idle()

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
                .compose(specificIntentUseCase)
        )
    }

    override val reducer: Reducer<CardListViewState> = LambdaReducer {
        when (this) {
            is CardListUseCase.Result -> when (this) {
                is CardListUseCase.Result.Success -> it.copy(cards = cards, progress = false)
                CardListUseCase.Result.Idle -> it.copy(progress = true)
                is CardListUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is SpecificIntentUseCase.Result -> when (intent as CardListIntent) {
                CardListIntent.OnStart -> it
                CardListIntent.OnAddCardClicked -> it.copy(showAddCardDialog = true)
                CardListIntent.OnAddCardDialogDismissed -> it.copy(showAddCardDialog = false)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}