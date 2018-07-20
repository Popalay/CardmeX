package com.popalay.cardme.main

import com.gojuno.koptional.None
import com.popalay.cardme.base.state.BaseMviViewModel
import com.popalay.cardme.base.state.IntentProcessor
import com.popalay.cardme.base.state.LambdaReducer
import com.popalay.cardme.base.state.Processor
import com.popalay.cardme.base.state.Reducer
import com.popalay.cardme.login.usecase.GetCurrentUserUseCase
import com.popalay.cardme.login.usecase.LogOutUseCase
import io.reactivex.rxkotlin.ofType

internal class MainViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : BaseMviViewModel<MainViewState, MainIntent>() {

    override val initialState: MainViewState = MainViewState.idle()

    override val processor: Processor<MainIntent> = IntentProcessor {
        listOf(
            it.ofType<MainIntent.OnStarted>()
                .map { GetCurrentUserUseCase.Action }
                .compose(getCurrentUserUseCase),
            it.ofType<MainIntent.OnUnsyncClicked>()
                .map { LogOutUseCase.Action }
                .compose(logOutUseCase)
        )
    }

    override val reducer: Reducer<MainViewState> = LambdaReducer {
        when (this) {
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(user = user)
                GetCurrentUserUseCase.Result.Idle -> it.copy(isProgress = true)
                is GetCurrentUserUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is LogOutUseCase.Result -> when (this) {
                LogOutUseCase.Result.Success -> it.copy(user = None)
                LogOutUseCase.Result.Idle -> it.copy(isProgress = true)
                is LogOutUseCase.Result.Failure -> it.copy(error = throwable)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}