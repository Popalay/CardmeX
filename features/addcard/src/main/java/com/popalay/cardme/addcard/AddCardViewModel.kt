package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.*
import com.popalay.cardme.api.core.model.DisplayName
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
    private val specificIntentUseCase: SpecificIntentUseCase
) : BaseMviViewModel<AddCardViewState, AddCardIntent>() {

    override val initialState: AddCardViewState = AddCardViewState()

    override val processor: Processor<AddCardIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<AddCardIntent.OnStart>()
                .filter { isUserCard }
                .map { GetCurrentUserUseCase.Action }
                .compose(getCurrentUserUseCase),
            observable.ofType<AddCardIntent.SaveClicked>()
                .filter { !isUserCard }
                .map { SaveCardUseCase.Action(it.number, it.name, it.isPublic, it.cardType, it.selectedUser) }
                .compose(saveCardUseCase),
            observable.ofType<AddCardIntent.PeopleClicked>()
                .filter { !isUserCard && !it.peopleShowed }
                .map { UserListUseCase.Action("", "", 100) }
                .compose(userListUseCase),
            observable.ofType<AddCardIntent.PeopleClicked>()
                .filter { !isUserCard && it.peopleShowed }
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<AddCardIntent.CrossClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
            observable.ofType<AddCardIntent.OnUserClicked>()
                .map { SpecificIntentUseCase.Action(it) }
                .compose(specificIntentUseCase),
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
                is SaveCardUseCase.Result.Success -> it.copy(saved = true, saveProgress = false)
                SaveCardUseCase.Result.Idle -> it.copy(saveProgress = true)
                is SaveCardUseCase.Result.Failure -> it.copy(error = throwable, saveProgress = false)
            }
            is SaveUserCardUseCase.Result -> when (this) {
                is SaveUserCardUseCase.Result.Success -> it.copy(saved = true, saveProgress = false)
                SaveUserCardUseCase.Result.Idle -> it.copy(saveProgress = true)
                is SaveUserCardUseCase.Result.Failure -> it.copy(error = throwable, saveProgress = false)
            }
            is ValidateCardUseCase.Result -> when (this) {
                is ValidateCardUseCase.Result.Success -> it.copy(isValid = isValid, saveProgress = false)
                is ValidateCardUseCase.Result.Idle -> it.copy(
                    holderName = name,
                    cardNumber = number,
                    selectedUser = it.selectedUser?.copy(displayName = DisplayName(name), card = it.selectedUser.card?.copy(number = number)),
                    isPublic = isPublic
                )
                is ValidateCardUseCase.Result.Failure -> it.copy(isValid = false, error = throwable, saveProgress = false)
            }
            is IdentifyCardNumberUseCase.Result -> when (this) {
                is IdentifyCardNumberUseCase.Result.Success -> it.copy(cardType = cardType)
                IdentifyCardNumberUseCase.Result.Idle -> it
                is IdentifyCardNumberUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(
                    selectedUser = user,
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
                        selectedUser = null,
                        showClearButton = false,
                        isHolderNameEditable = true,
                        isCardNumberEditable = true,
                        holderName = "",
                        cardNumber = ""
                    )
                    is AddCardIntent.PeopleClicked -> it.copy(users = null)
                    is AddCardIntent.OnUserClicked -> it.copy(
                        selectedUser = user,
                        showClearButton = true,
                        users = null,
                        isHolderNameEditable = false,
                        isCardNumberEditable = false
                    )
                    else -> throw UnsupportedOperationException()
                }
            }
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}