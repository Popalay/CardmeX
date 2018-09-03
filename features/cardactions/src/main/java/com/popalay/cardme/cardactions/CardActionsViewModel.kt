package com.popalay.cardme.cardactions

import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.cardactions.usecase.ShareCardUseCase
import com.popalay.cardme.core.state.BaseMviViewModel
import io.reactivex.rxkotlin.ofType

internal class CardActionsViewModel(
    private val cardId: String,
    private val shareCardUseCase: ShareCardUseCase
) : BaseMviViewModel<CardActionsViewState, CardActionsIntent>() {

    override val initialState: CardActionsViewState = CardActionsViewState()

    override val processor: Processor<CardActionsIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<CardActionsIntent.OnShareClicked>()
                .map { ShareCardUseCase.Action(cardId) }
                .compose(shareCardUseCase)/*,
            observable.ofType<CardActionsIntent.OnRemoveClicked>()
                .filter { !isUserCard }
                .map { SaveCardUseCase.Action(it.number, it.name, it.isPublic, it.cardType) }
                .compose(saveCardUseCase)*/
        )
    }

    override val reducer: Reducer<CardActionsViewState> = LambdaReducer {
        when (this) {
            is ShareCardUseCase.Result -> when (this) {
                is ShareCardUseCase.Result.Success -> it.copy(completed = true)
                ShareCardUseCase.Result.Idle -> it
                is ShareCardUseCase.Result.Failure -> it.copy(error = throwable)
            }
            /*is SaveUserCardUseCase.Result -> when (this) {
                is SaveUserCardUseCase.Result.Success -> it.copy(saved = true, progress = false)
                SaveUserCardUseCase.Result.Idle -> it.copy(progress = true)
                is SaveUserCardUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is ValidateCardUseCase.Result -> when (this) {
                is ValidateCardUseCase.Result.Success -> it.copy(isValid = isValid, progress = false)
                ValidateCardUseCase.Result.Idle -> it
                is ValidateCardUseCase.Result.Failure -> it.copy(isValid = false, error = throwable, progress = false)
            }
            is IdentifyCardNumberUseCase.Result -> when (this) {
                is IdentifyCardNumberUseCase.Result.Success -> it.copy(cardType = cardType)
                IdentifyCardNumberUseCase.Result.Idle -> it
                is IdentifyCardNumberUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(
                    holderName = requireNotNull(user.toNullable()).displayName,
                    number = user.toNullable()?.card?.number ?: "",
                    isPublicEditable = false,
                    isHolderNameEditable = false
                )
                GetCurrentUserUseCase.Result.Idle -> it
                is GetCurrentUserUseCase.Result.Failure -> it.copy(error = throwable)
            }*/
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}