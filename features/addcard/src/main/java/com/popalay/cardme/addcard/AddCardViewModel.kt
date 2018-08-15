package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.SaveCardUseCase
import com.popalay.cardme.addcard.usecase.ValidateCardUseCase
import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import io.reactivex.rxkotlin.ofType

internal class AddCardViewModel(
    private val saveCardUseCase: SaveCardUseCase,
    private val validateCardUseCase: ValidateCardUseCase
) : BaseMviViewModel<AddCardViewState, AddCardIntent>() {

    override val initialState: AddCardViewState = AddCardViewState.idle()

    override val processor: Processor<AddCardIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<AddCardIntent.SaveClicked>()
                .map { SaveCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(saveCardUseCase),
            observable.ofType<AddCardIntent.NumberChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(validateCardUseCase),
            observable.ofType<AddCardIntent.NameChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(validateCardUseCase)
        )
    }

    override val reducer: Reducer<AddCardViewState> = LambdaReducer {
        when (this) {
            is SaveCardUseCase.Result -> when (this) {
                is SaveCardUseCase.Result.Success -> it.copy(saved = true, progress = false)
                SaveCardUseCase.Result.Idle -> it.copy(progress = true)
                is SaveCardUseCase.Result.Failure -> it.copy(numberError = throwable, progress = false)
            }
            is ValidateCardUseCase.Result -> when (this) {
                is ValidateCardUseCase.Result.Success -> it.copy(isValid = isValid, progress = false)
                ValidateCardUseCase.Result.Idle -> it.copy(progress = false)
                is ValidateCardUseCase.Result.Failure -> it.copy(isValid = false, numberError = throwable, progress = false)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}