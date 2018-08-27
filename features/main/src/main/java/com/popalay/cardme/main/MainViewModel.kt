package com.popalay.cardme.main

import com.gojuno.koptional.None
import com.popalay.cardme.api.navigation.Router
import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.GetCurrentUserUseCase
import com.popalay.cardme.core.usecase.LogOutUseCase
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import com.popalay.cardme.main.auth.CardmeAuthCredentials
import com.popalay.cardme.main.auth.CardmeAuthResult
import com.popalay.cardme.main.usecase.AuthUseCase
import com.popalay.cardme.main.usecase.HandleAuthResultUseCase
import io.reactivex.rxkotlin.ofType

internal class MainViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val authUseCase: AuthUseCase,
    private val handleAuthResultUseCase: HandleAuthResultUseCase,
    private val intentUseCase: SpecificIntentUseCase,
    private val router: Router
) : BaseMviViewModel<MainViewState, MainIntent>() {

    override val initialState: MainViewState = MainViewState.idle()

    override val processor: Processor<MainIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<MainIntent.OnStarted>()
                .map { GetCurrentUserUseCase.Action }
                .compose(getCurrentUserUseCase),
            observable.ofType<MainIntent.OnUnsyncClicked>()
                .map { LogOutUseCase.Action }
                .compose(logOutUseCase),
            observable.ofType<MainIntent.OnSyncClicked>()
                .map { AuthUseCase.Action(CardmeAuthCredentials.Google) }
                .compose(authUseCase),
            observable.ofType<MainIntent.OnActivityResult>()
                .map { HandleAuthResultUseCase.Action(CardmeAuthResult.Google(it.success, it.requestCode, it.data)) }
                .compose(handleAuthResultUseCase),
            observable.ofType<MainIntent.OnUserClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(intentUseCase)
        )
    }

    override val reducer: Reducer<MainViewState> = LambdaReducer {
        when (this) {
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(user = user, isSyncProgress = false)
                GetCurrentUserUseCase.Result.Idle -> it.copy(isSyncProgress = true)
                is GetCurrentUserUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is LogOutUseCase.Result -> when (this) {
                LogOutUseCase.Result.Success -> it.copy(user = None, isSyncProgress = false)
                LogOutUseCase.Result.Idle -> it.copy(isSyncProgress = true)
                is LogOutUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is AuthUseCase.Result -> when (this) {
                is AuthUseCase.Result.Success -> it.copy(user = user, isSyncProgress = false)
                AuthUseCase.Result.Idle -> it.copy(isSyncProgress = true)
                is AuthUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is HandleAuthResultUseCase.Result -> when (this) {
                is HandleAuthResultUseCase.Result.Success -> it.copy(user = user, isSyncProgress = false)
                HandleAuthResultUseCase.Result.Idle -> it.copy(isSyncProgress = true)
                is HandleAuthResultUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is SpecificIntentUseCase.Result -> {
                if (intent is MainIntent.OnUserClicked) router.navigate(MainDestination.UserCard)
                it
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}