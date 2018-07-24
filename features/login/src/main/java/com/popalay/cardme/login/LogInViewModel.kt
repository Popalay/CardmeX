package com.popalay.cardme.login

import com.gojuno.koptional.Some
import com.popalay.cardme.api.navigation.Router
import com.popalay.cardme.api.state.IntentProcessor
import com.popalay.cardme.api.state.LambdaReducer
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.api.usecase.UseCase
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.login.auth.CardmeAuthCredentials
import com.popalay.cardme.login.auth.CardmeAuthResult
import com.popalay.cardme.login.usecase.AuthUseCase
import com.popalay.cardme.login.usecase.HandleAuthResultUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

internal class LogInViewModel(
    private val router: Router,
    private val authUseCase: AuthUseCase,
    private val handleAuthResultUseCase: HandleAuthResultUseCase
) : BaseMviViewModel<LogInViewState, LogInIntent>() {

    override val initialState: LogInViewState = LogInViewState.idle()

    override val processor: Processor<LogInIntent> = IntentProcessor {
        listOf(
            it.ofType<LogInIntent.GoogleLogInClicked>()
                .map { AuthUseCase.Action(CardmeAuthCredentials.Google) }
                .compose(authUseCase)
                .switchMap { createShowUserAnimationObservable(it) },
            it.ofType<LogInIntent.OnActivityResult>()
                .map { HandleAuthResultUseCase.Action(CardmeAuthResult.Google(it.success, it.requestCode, it.data)) }
                .compose(handleAuthResultUseCase)
                .switchMap { createShowUserAnimationObservable(it) }
        )
    }

    override val reducer: Reducer<LogInViewState> = LambdaReducer {
        when (this) {
            is AuthUseCase.Result -> when (this) {
                is AuthUseCase.Result.Success -> it.copy(user = user, isProgress = false)
                AuthUseCase.Result.Idle -> it.copy(isProgress = true)
                is AuthUseCase.Result.Failure -> it.copy(error = throwable, isProgress = false)
            }
            is HandleAuthResultUseCase.Result -> when (this) {
                is HandleAuthResultUseCase.Result.Success -> it.copy(user = user, isProgress = false)
                HandleAuthResultUseCase.Result.Idle -> it.copy(isProgress = true)
                is HandleAuthResultUseCase.Result.Failure -> it.copy(error = throwable, isProgress = false)
            }
            is ShowUserAnimationResult -> when (this) {
                ShowUserAnimationResult.Started -> it.copy(showUserInfo = true)
                ShowUserAnimationResult.Completed -> {
                    router.navigateUp()
                    it
                }
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }

    private fun createShowUserAnimationObservable(result: UseCase.Result) =
        Observable.just(result)
            .filter { (result as? AuthUseCase.Result.Success)?.user is Some || (result as? HandleAuthResultUseCase.Result.Success)?.user is Some }
            .flatMap {
                Observable.timer(1000L, TimeUnit.MILLISECONDS, Schedulers.computation())
                    .map { ShowUserAnimationResult.Completed }
                    .cast(UseCase.Result::class.java)
                    .startWith(ShowUserAnimationResult.Started)
            }
            .startWith(result)
}

sealed class ShowUserAnimationResult : UseCase.Result {
    object Started : ShowUserAnimationResult()
    object Completed : ShowUserAnimationResult()
}