package com.popalay.cardme.cardactions

import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.cardactions.usecase.RemoveCardUseCase
import com.popalay.cardme.cardactions.usecase.ShareCardUseCase
import com.popalay.cardme.core.state.BaseMviViewModel
import io.reactivex.rxkotlin.ofType

internal class CardActionsViewModel(
    private val cardId: String,
    private val shareCardUseCase: ShareCardUseCase,
    private val removeCardUseCase: RemoveCardUseCase
) : BaseMviViewModel<CardActionsViewState, CardActionsIntent>() {

    override val initialState: CardActionsViewState = CardActionsViewState()

    override val processor: Processor<CardActionsIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<CardActionsIntent.OnShareClicked>()
                .map { ShareCardUseCase.Action(cardId) }
                .compose(shareCardUseCase),
            observable.ofType<CardActionsIntent.OnRemoveClicked>()
                .map { RemoveCardUseCase.Action(cardId) }
                .compose(removeCardUseCase)
        )
    }

    override val reducer: Reducer<CardActionsViewState> = LambdaReducer {
        when (this) {
            is ShareCardUseCase.Result -> when (this) {
                is ShareCardUseCase.Result.Success -> it.copy(completed = true)
                ShareCardUseCase.Result.Idle -> it
                is ShareCardUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is RemoveCardUseCase.Result -> when (this) {
                is RemoveCardUseCase.Result.Success -> it.copy(completed = true)
                RemoveCardUseCase.Result.Idle -> it
                is RemoveCardUseCase.Result.Failure -> it.copy(error = throwable)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}