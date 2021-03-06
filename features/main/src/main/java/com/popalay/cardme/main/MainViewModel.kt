package com.popalay.cardme.main

import com.popalay.cardme.api.ui.navigation.Router
import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.GetCurrentUserUseCase
import com.popalay.cardme.core.usecase.LogOutUseCase
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import com.popalay.cardme.main.usecase.AuthUseCase
import com.popalay.cardme.main.usecase.HandleAuthResultUseCase
import com.popalay.cardme.main.usecase.SyncTokenUseCase
import io.reactivex.rxkotlin.ofType

internal class MainViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val authUseCase: AuthUseCase,
    private val handleAuthResultUseCase: HandleAuthResultUseCase,
    private val specificIntentUseCase: SpecificIntentUseCase,
    private val syncTokenUseCase: SyncTokenUseCase,
    private val router: Router
) : BaseMviViewModel<MainViewState, MainIntent>() {

    override val initialState: MainViewState = MainViewState()

    override val processor: Processor<MainIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<MainIntent.OnStarted>()
                .map { GetCurrentUserUseCase.Action }
                .compose(getCurrentUserUseCase),
            observable.ofType<MainIntent.OnStarted>()
                .map { SyncTokenUseCase.Action }
                .compose(syncTokenUseCase),
            observable.ofType<MainIntent.OnAddCardClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<MainIntent.OnUnsyncClicked>()
                .map { LogOutUseCase.Action }
                .compose(logOutUseCase),
            observable.ofType<MainIntent.OnSyncClicked>()
                .map { AuthUseCase.Action }
                .compose(authUseCase),
            observable.ofType<MainIntent.OnActivityResult>()
                .map { HandleAuthResultUseCase.Action(it.success, it.requestCode, it.data) }
                .compose(handleAuthResultUseCase),
            observable.ofType<MainIntent.OnUserClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase)
        )
    }

    override val reducer: Reducer<MainViewState> = LambdaReducer {
        when (this) {
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(user = user, isSyncProgress = false)
                GetCurrentUserUseCase.Result.Idle -> it
                is GetCurrentUserUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is LogOutUseCase.Result -> when (this) {
                LogOutUseCase.Result.Success -> it.copy(isSyncProgress = false)
                LogOutUseCase.Result.Idle -> it.copy(isSyncProgress = true)
                is LogOutUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is AuthUseCase.Result -> when (this) {
                AuthUseCase.Result.Success -> it.copy(isSyncProgress = false)
                AuthUseCase.Result.Idle -> it.copy(isSyncProgress = true)
                is AuthUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is HandleAuthResultUseCase.Result -> when (this) {
                HandleAuthResultUseCase.Result.Success -> it.copy(isSyncProgress = false)
                HandleAuthResultUseCase.Result.Idle -> it.copy(isSyncProgress = true)
                is HandleAuthResultUseCase.Result.Failure -> it.copy(error = throwable, isSyncProgress = false)
            }
            is SyncTokenUseCase.Result -> it
            is SpecificIntentUseCase.Result -> with(intent as MainIntent) {
                when (this) {
                    MainIntent.OnAddCardClicked -> {
                        router.navigate(MainDestination.AddCard)
                        it
                    }
                    MainIntent.OnUserClicked -> {
                        router.navigate(MainDestination.UserCard)
                        it
                    }
                    else -> throw UnsupportedOperationException()
                }
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}