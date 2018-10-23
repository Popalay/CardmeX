package com.popalay.cardme.usercard

import com.popalay.cardme.api.ui.navigation.Router
import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.CopyCardNumberUseCase
import com.popalay.cardme.core.usecase.ShareCardUseCase
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import com.popalay.cardme.usercard.usecase.GetCurrentUserWithCardUseCase
import io.reactivex.rxkotlin.ofType

internal class UserCardViewModel(
    private val getCurrentUserWithCardUseCase: GetCurrentUserWithCardUseCase,
    private val specificIntentUseCase: SpecificIntentUseCase,
    private val shareCardUseCase: ShareCardUseCase,
    private val copyCardNumberUseCase: CopyCardNumberUseCase,
    private val router: Router
) : BaseMviViewModel<UserCardViewState, UserCardIntent>() {

    override val initialState: UserCardViewState = UserCardViewState()

    override val processor: Processor<UserCardIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<UserCardIntent.OnStart>()
                .map { GetCurrentUserWithCardUseCase.Action }
                .compose(getCurrentUserWithCardUseCase),
            observable.ofType<UserCardIntent.OnSkipClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<UserCardIntent.OnEditClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<UserCardIntent.OnAddClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<UserCardIntent.OnShareClicked>()
                .map { ShareCardUseCase.Action(it.card.id) }
                .compose(shareCardUseCase),
            observable.ofType<UserCardIntent.OnCardClicked>()
                .map { CopyCardNumberUseCase.Action(it.card) }
                .compose(copyCardNumberUseCase)
        )
    }

    override val reducer: Reducer<UserCardViewState> = LambdaReducer {
        when (this) {
            is GetCurrentUserWithCardUseCase.Result -> when (this) {
                is GetCurrentUserWithCardUseCase.Result.Success -> it.copy(user = user, card = card, progress = false)
                GetCurrentUserWithCardUseCase.Result.Idle -> it.copy(progress = true)
                is GetCurrentUserWithCardUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            is CopyCardNumberUseCase.Result -> when (this) {
                is CopyCardNumberUseCase.Result.Success -> it.copy(toastMessage = "The card in the clipboard")
                CopyCardNumberUseCase.Result.Idle -> it
                is CopyCardNumberUseCase.Result.Failure -> it.copy(error = throwable)
                CopyCardNumberUseCase.Result.HideMessage -> it.copy(toastMessage = null)
            }
            is ShareCardUseCase.Result -> when (this) {
                is ShareCardUseCase.Result.Success -> it
                ShareCardUseCase.Result.Idle -> it
                is ShareCardUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is SpecificIntentUseCase.Result -> when (intent as UserCardIntent) {
                UserCardIntent.OnEditClicked -> {
                    router.navigate(UserCardDestination.AddCard)
                    it
                }
                UserCardIntent.OnAddClicked -> {
                    router.navigate(UserCardDestination.AddCard)
                    it
                }
                UserCardIntent.OnSkipClicked -> {
                    router.navigateUp()
                    it
                }
                else -> throw UnsupportedOperationException()
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}