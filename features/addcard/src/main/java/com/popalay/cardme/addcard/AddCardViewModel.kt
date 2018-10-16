package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.*
import com.popalay.cardme.api.core.model.DisplayName
import com.popalay.cardme.api.ui.navigation.Router
import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.GetCurrentUserUseCase
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.ofType

internal class AddCardViewModel(
    private val isUserCard: Boolean,
    private val saveCardUseCase: SaveCardUseCase,
    private val validateCardUseCase: ValidateCardUseCase,
    private val identifyCardNumberUseCase: IdentifyCardNumberUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val saveUserCardUseCase: SaveUserCardUseCase,
    private val userListUseCase: UserListUseCase,
    private val specificIntentUseCase: SpecificIntentUseCase,
    private val router: Router
) : BaseMviViewModel<AddCardViewState, AddCardIntent>() {

    override val initialState: AddCardViewState = AddCardViewState()

    override val processor: Processor<AddCardIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<AddCardIntent.OnStart>()
                .filter { isUserCard }
                .map { GetCurrentUserUseCase.Action }
                .compose(getCurrentUserUseCase),
            observable.ofType<AddCardIntent.OnStart>()
                .filter { !isUserCard }
                .map { UserListUseCase.Action("", "", 100) }
                .compose(userListUseCase),
            observable.ofType<AddCardIntent.SaveClicked>()
                .filter { !isUserCard }
                .map { SaveCardUseCase.Action(it.number, it.name, it.isPublic, it.cardType) }
                .compose(saveCardUseCase),
            observable.ofType<AddCardIntent.CrossClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            //TODO send request
/*            observable.ofType<AddCardIntent.OnUserClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),*/
            observable.ofType<AddCardIntent.SaveClicked>()
                .filter { isUserCard }
                .map { SaveUserCardUseCase.Action(it.number, it.name, it.isPublic, it.cardType) }
                .compose(saveUserCardUseCase),
            observable.ofType<AddCardIntent.NumberChanged>()
                .map { IdentifyCardNumberUseCase.Action(it.number) }
                .compose(identifyCardNumberUseCase),
            Observables.combineLatest(
                observable.ofType<AddCardIntent.NumberChanged>(),
                observable.ofType<AddCardIntent.NameChanged>(),
                observable.ofType<AddCardIntent.IsPublicChanged>()
            )
                .map { ValidateCardUseCase.Action(it.first.number, it.second.name, it.third.isPublic) }
                .compose(validateCardUseCase)
        )
    }

    override val reducer: Reducer<AddCardViewState> = LambdaReducer {
        when (this) {
            is SaveCardUseCase.Result -> when (this) {
                is SaveCardUseCase.Result.Success -> {
                    router.navigateUp()
                    it
                }
                SaveCardUseCase.Result.Idle -> it.copy(saveProgress = true)
                is SaveCardUseCase.Result.Failure -> it.copy(error = throwable, saveProgress = false)
            }
            is SaveUserCardUseCase.Result -> when (this) {
                is SaveUserCardUseCase.Result.Success -> {
                    router.navigateUp()
                    it
                }
                SaveUserCardUseCase.Result.Idle -> it.copy(saveProgress = true)
                is SaveUserCardUseCase.Result.Failure -> it.copy(error = throwable, saveProgress = false)
            }
            is ValidateCardUseCase.Result -> when (this) {
                is ValidateCardUseCase.Result.Success -> it.copy(
                    isValid = isValid,
                    saveProgress = false,
                    showClearButton = isValid
                )
                is ValidateCardUseCase.Result.Idle -> it.copy(
                    holderName = name,
                    cardNumber = number,
                    isPublic = isPublic
                )
                is ValidateCardUseCase.Result.Failure -> it.copy(isValid = false, error = throwable, saveProgress = false)
            }
            is IdentifyCardNumberUseCase.Result -> when (this) {
                is IdentifyCardNumberUseCase.Result.Success -> it.copy(cardType = cardType)
                IdentifyCardNumberUseCase.Result.Idle -> it
                is IdentifyCardNumberUseCase.Result.Failure -> it.copy(error = throwable)
            }
            //TODO set user data
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(
                    isPublicEditable = false,
                    isHolderNameEditable = false
                )
                GetCurrentUserUseCase.Result.Idle -> it
                is GetCurrentUserUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is UserListUseCase.Result -> when (this) {
                is UserListUseCase.Result.Success -> it.copy(users = users, peopleProgress = false)
                UserListUseCase.Result.Idle -> it.copy(peopleProgress = true)
                is UserListUseCase.Result.Failure -> it.copy(error = throwable, peopleProgress = false)
            }
            is SpecificIntentUseCase.Result -> with(intent as AddCardIntent) {
                when (this) {
                    AddCardIntent.CrossClicked -> it.copy(
                        showClearButton = false,
                        isHolderNameEditable = true,
                        isCardNumberEditable = true,
                        holderName = "",
                        cardNumber = ""
                    )
                    else -> throw UnsupportedOperationException()
                }
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}