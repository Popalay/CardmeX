package com.popalay.cardme.login

import com.gojuno.koptional.Some
import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.login.auth.CardmeAuthCredentials
import com.popalay.cardme.login.auth.CardmeAuthResult
import com.popalay.cardme.login.usecase.AuthUseCase
import com.popalay.cardme.login.usecase.HandleAuthResultUseCase
import io.reactivex.rxkotlin.ofType

internal class LogInViewModel(
    private val authUseCase: AuthUseCase,
    private val handleAuthResultUseCase: HandleAuthResultUseCase
) : BaseMviViewModel<LogInViewState, LogInIntent>() {

    override val initialState: LogInViewState = LogInViewState.idle()

    override val processor: Processor<LogInIntent> = IntentProcessor {
        listOf(
            it.ofType<LogInIntent.GoogleLogInClicked>()
                .map { AuthUseCase.Action(CardmeAuthCredentials.Google) }
                .compose(authUseCase),
            it.ofType<LogInIntent.OnActivityResult>()
                .map { HandleAuthResultUseCase.Action(CardmeAuthResult.Google(it.success, it.requestCode, it.data)) }
                .compose(handleAuthResultUseCase)
        )
    }

    override val reducer: Reducer<LogInViewState> = LambdaReducer {
        when (this) {
            is AuthUseCase.Result -> when (this) {
                is AuthUseCase.Result.Success -> it.copy(user = user, canStart = user is Some, isProgress = false)
                AuthUseCase.Result.Idle -> it.copy(isProgress = true)
                is AuthUseCase.Result.Failure -> it.copy(error = throwable, canStart = false, isProgress = false)
            }
            is HandleAuthResultUseCase.Result -> when (this) {
                is HandleAuthResultUseCase.Result.Success -> it.copy(user = user, canStart = user is Some, isProgress = false)
                HandleAuthResultUseCase.Result.Idle -> it.copy(isProgress = true)
                is HandleAuthResultUseCase.Result.Failure -> it.copy(error = throwable, canStart = false, isProgress = false)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}