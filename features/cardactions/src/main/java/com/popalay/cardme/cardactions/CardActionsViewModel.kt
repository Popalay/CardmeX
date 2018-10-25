package com.popalay.cardme.cardactions

import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.cardactions.usecase.RemoveCardUseCase
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.ShareCardUseCase
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
                is ShareCardUseCase.Result.Success -> it.copy(completed = true, progress = false)
                ShareCardUseCase.Result.Idle -> it.copy(progress = true)
                is ShareCardUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is RemoveCardUseCase.Result -> when (this) {
                is RemoveCardUseCase.Result.Success -> it.copy(completed = true, progress = false)
                RemoveCardUseCase.Result.Idle -> it.copy(progress = true)
                is RemoveCardUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}