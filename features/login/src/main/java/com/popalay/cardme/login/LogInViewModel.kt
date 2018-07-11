package com.popalay.cardme.login

import com.popalay.cardme.base.state.*
import com.popalay.cardme.login.usecase.ValidatePhoneNumberUseCase
import io.reactivex.rxkotlin.ofType

class LogInViewModel(
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase
) : BaseMviViewModel<LogInViewState, LogInIntent>() {

    override val initialState: LogInViewState = LogInViewState.idle()

    override val processor: Processor<LogInIntent> = IntentProcessor {
        listOf(
            it.ofType<LogInIntent.PhoneNumberChanged>()
                .map { ValidatePhoneNumberUseCase.Action(it.phoneNumber) }
                .compose(validatePhoneNumberUseCase)
/*            it.ofType<LogInIntent.GetStartedClicked>()
                .map { ValidateDebtUseCase.Action(it.debt) }
                .compose(validateDebtUseCase)*/
        )
    }

    override val reducer: Reducer<LogInViewState> = LambdaReducer {
        when (this) {
            is ValidatePhoneNumberUseCase.Result -> when (this) {
                is ValidatePhoneNumberUseCase.Result.Success -> it.copy(canStart = valid)
                is ValidatePhoneNumberUseCase.Result.Idle -> it.copy(phoneNumber = phoneNumber)
                is ValidatePhoneNumberUseCase.Result.Failure -> it.copy(error = throwable)
            }
            else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
        }
    }
}