package com.popalay.cardme.login

import com.gojuno.koptional.Some
import com.popalay.cardme.base.state.*
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
                .map { AuthUseCase.Action(AuthCredentials.Google) }
                .compose(authUseCase),
            it.ofType<LogInIntent.OnActivityResult>()
                .map { HandleAuthResultUseCase.Action(AuthResult.Google(it.requestCode, it.data)) }
                .compose(handleAuthResultUseCase)
        )
    }

    override val reducer: Reducer<LogInViewState> = LambdaReducer {
        when (this) {
            is AuthUseCase.Result -> when (this) {
                is AuthUseCase.Result.Success -> it.copy(user = user, canStart = user is Some)
                AuthUseCase.Result.Idle -> it.copy(isProgress = true)
                is AuthUseCase.Result.Failure -> it.copy(error = throwable, canStart = false)
            }
            is HandleAuthResultUseCase.Result -> when (this) {
                is HandleAuthResultUseCase.Result.Success -> it.copy(user = user, canStart = user is Some)
                HandleAuthResultUseCase.Result.Idle -> it.copy(isProgress = true)
                is HandleAuthResultUseCase.Result.Failure -> it.copy(error = throwable, canStart = false)
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}