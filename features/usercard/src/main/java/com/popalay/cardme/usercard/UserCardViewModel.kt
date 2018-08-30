package com.popalay.cardme.usercard

import com.popalay.cardme.api.navigation.Router
import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import com.popalay.cardme.usercard.usecase.GetUserCardUseCase
import io.reactivex.rxkotlin.ofType

internal class UserCardViewModel(
    private val getUserCardUseCase: GetUserCardUseCase,
    private val specificIntentUseCase: SpecificIntentUseCase,
    private val router: Router
) : BaseMviViewModel<UserCardViewState, UserCardIntent>() {

    override val initialState: UserCardViewState = UserCardViewState.idle()

    override val processor: Processor<UserCardIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<UserCardIntent.OnStart>()
                .map { GetUserCardUseCase.Action }
                .compose(getUserCardUseCase),
            observable.ofType<UserCardIntent.OnSkipClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase)
/*            observable.ofType<UserCardIntent.OnEditClicked>()
                .map { SaveCardUseCase.Action(it.number, it.name, it.isPublic, it.cardType) }
                .compose(saveCardUseCase),
            observable.ofType<UserCardIntent.NumberChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(validateCardUseCase),
            observable.ofType<UserCardIntent.NumberChanged>()
                .map { IdentifyCardNumberUseCase.Action(it.number) }
                .compose(identifyCardNumberUseCase),
            observable.ofType<UserCardIntent.NumberChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(validateCardUseCase),
            observable.ofType<UserCardIntent.NameChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(validateCardUseCase)*/
        )
    }

    override val reducer: Reducer<UserCardViewState> = LambdaReducer {
        when (this) {
            is GetUserCardUseCase.Result -> when (this) {
                is GetUserCardUseCase.Result.Success -> it.copy(card = card, user = user)
                GetUserCardUseCase.Result.Idle -> it
                is GetUserCardUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is SpecificIntentUseCase.Result -> when (intent as UserCardIntent) {
                UserCardIntent.OnEditClicked -> TODO()
                UserCardIntent.OnAddClicked -> TODO()
                UserCardIntent.OnSkipClicked -> {
                    router.navigateUp()
                    it
                }
                UserCardIntent.OnStart -> throw UnsupportedOperationException()
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}