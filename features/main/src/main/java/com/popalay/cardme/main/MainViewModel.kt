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
import com.popalay.cardme.core.usecase.NavigationUseCase
import io.reactivex.rxkotlin.ofType

internal class MainViewModel(
    private val router: Router,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val navigationUseCase: NavigationUseCase
) : BaseMviViewModel<MainViewState, MainIntent>() {

    override val initialState: MainViewState = MainViewState.idle()

    override val processor: Processor<MainIntent> = IntentProcessor {
        listOf(
            it.ofType<MainIntent.OnStarted>()
                .map { GetCurrentUserUseCase.Action }
                .compose(getCurrentUserUseCase),
            it.ofType<MainIntent.OnUnsyncClicked>()
                .map { LogOutUseCase.Action }
                .compose(logOutUseCase),
            it.ofType<MainIntent.OnSyncClicked>()
                .doOnNext { router.navigate(MainDestination.LogIn) }
                .map { NavigationUseCase.Action }
                .compose(navigationUseCase)
        )
    }

    override val reducer: Reducer<MainViewState> = LambdaReducer {
        when (this) {
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(user = user, isUnsyncProgress = false)
                GetCurrentUserUseCase.Result.Idle -> it.copy(isUnsyncProgress = true)
                is GetCurrentUserUseCase.Result.Failure -> it.copy(error = throwable, isUnsyncProgress = false)
            }
            is LogOutUseCase.Result -> when (this) {
                LogOutUseCase.Result.Success -> it.copy(user = None, isUnsyncProgress = false)
                LogOutUseCase.Result.Idle -> it.copy(isUnsyncProgress = true)
                is LogOutUseCase.Result.Failure -> it.copy(error = throwable, isUnsyncProgress = false)
            }
            is NavigationUseCase.Result -> it
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}