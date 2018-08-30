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

    override val initialState: UserCardViewState = UserCardViewState()

    override val processor: Processor<UserCardIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<UserCardIntent.OnStart>()
                .map { GetUserCardUseCase.Action }
                .compose(getUserCardUseCase),
            observable.ofType<UserCardIntent.OnSkipClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<UserCardIntent.OnAddClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<UserCardIntent.OnAddCardDialogDismissed>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase)
        )
    }

    override val reducer: Reducer<UserCardViewState> = LambdaReducer {
        when (this) {
            is GetUserCardUseCase.Result -> when (this) {
                is GetUserCardUseCase.Result.Success -> it.copy(card = card, user = user, progress = false)
                GetUserCardUseCase.Result.Idle -> it.copy(progress = true)
                is GetUserCardUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is SpecificIntentUseCase.Result -> when (intent as UserCardIntent) {
                UserCardIntent.OnEditClicked -> TODO()
                UserCardIntent.OnAddClicked -> it.copy(showAddCardDialog = true)
                UserCardIntent.OnSkipClicked -> {
                    router.navigateUp()
                    it
                }
                is UserCardIntent.OnAddCardDialogDismissed -> {
                    if ((intent as UserCardIntent.OnAddCardDialogDismissed).isCardSaved) router.navigateUp()
                    it.copy(showAddCardDialog = false)
                }
                else -> throw UnsupportedOperationException()
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}