package com.popalay.cardme.usercard

import com.popalay.cardme.api.ui.navigation.Router
import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.GetCurrentUserUseCase
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import io.reactivex.rxkotlin.ofType

internal class UserCardViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val specificIntentUseCase: SpecificIntentUseCase,
    private val router: Router
) : BaseMviViewModel<UserCardViewState, UserCardIntent>() {

    override val initialState: UserCardViewState = UserCardViewState()

    override val processor: Processor<UserCardIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<UserCardIntent.OnStart>()
                .map { GetCurrentUserUseCase.Action }
                .compose(getCurrentUserUseCase),
            observable.ofType<UserCardIntent.OnSkipClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<UserCardIntent.OnEditClicked>()
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
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(user = user, progress = false)
                GetCurrentUserUseCase.Result.Idle -> it.copy(progress = true)
                is GetCurrentUserUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is SpecificIntentUseCase.Result -> when (intent as UserCardIntent) {
                UserCardIntent.OnEditClicked -> it.copy(showAddCardDialog = true)
                UserCardIntent.OnAddClicked -> it.copy(showAddCardDialog = true)
                UserCardIntent.OnSkipClicked -> {
                    router.navigateUp()
                    it
                }
                is UserCardIntent.OnAddCardDialogDismissed -> it.copy(showAddCardDialog = false)
                else -> throw UnsupportedOperationException()
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}