package com.popalay.cardme.carddetails

import com.popalay.cardme.api.ui.navigation.Router
import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.carddetails.usecase.GetCardUseCase
import com.popalay.cardme.carddetails.usecase.SaveCardUseCase
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.CopyCardNumberUseCase
import io.reactivex.rxkotlin.ofType

internal class CardDetailsViewModel(
    private val cardId: String,
    private val getCardUseCase: GetCardUseCase,
    private val saveCardUseCase: SaveCardUseCase,
    private val copyCardNumberUseCase: CopyCardNumberUseCase,
    private val router: Router
) : BaseMviViewModel<CardDetailsViewState, CardDetailsIntent>() {

    override val initialState: CardDetailsViewState = CardDetailsViewState()

    override val processor: Processor<CardDetailsIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<CardDetailsIntent.OnStart>()
                .map { GetCardUseCase.Action(cardId) }
                .compose(getCardUseCase),
            observable.ofType<CardDetailsIntent.OnAddClicked>()
                .map { SaveCardUseCase.Action(it.card) }
                .compose(saveCardUseCase),
            observable.ofType<CardDetailsIntent.OnCardClicked>()
                .map { CopyCardNumberUseCase.Action(it.card) }
                .compose(copyCardNumberUseCase)
        )
    }

    override val reducer: Reducer<CardDetailsViewState> = LambdaReducer {
        when (this) {
            is GetCardUseCase.Result -> when (this) {
                is GetCardUseCase.Result.Success -> it.copy(card = card, progress = false)
                GetCardUseCase.Result.Idle -> it.copy(progress = true)
                is GetCardUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is SaveCardUseCase.Result -> when (this) {
                is SaveCardUseCase.Result.Success -> {
                    router.popBackStack()
                    it.copy(saveProgress = false)
                }
                SaveCardUseCase.Result.Idle -> it.copy(saveProgress = true)
                is SaveCardUseCase.Result.Failure -> it.copy(error = throwable, saveProgress = false)
            }
            is CopyCardNumberUseCase.Result -> when (this) {
                is CopyCardNumberUseCase.Result.Success -> it.copy(toastMessage = "The card in the clipboard")
                CopyCardNumberUseCase.Result.Idle -> it
                is CopyCardNumberUseCase.Result.Failure -> it.copy(error = throwable)
                CopyCardNumberUseCase.Result.HideMessage -> it.copy(toastMessage = null)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}