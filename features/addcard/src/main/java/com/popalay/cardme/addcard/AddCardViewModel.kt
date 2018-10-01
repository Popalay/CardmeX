package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.*
import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.core.usecase.GetCurrentUserUseCase
import io.reactivex.rxkotlin.ofType

internal class AddCardViewModel(
    private val isUserCard: Boolean,
    private val saveCardUseCase: SaveCardUseCase,
    private val validateCardUseCase: ValidateCardUseCase,
    private val identifyCardNumberUseCase: IdentifyCardNumberUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val saveUserCardUseCase: SaveUserCardUseCase,
    private val userListUseCase: UserListUseCase
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
                .map { SaveCardUseCase.Action(it.number, it.name, it.isPublic, it.cardType) }
                .compose(saveCardUseCase),
            observable.ofType<AddCardIntent.PeopleClicked>()
                .filter { !isUserCard }
                .map { UserListUseCase.Action("", "", 100) }
                .compose(userListUseCase),
            observable.ofType<AddCardIntent.SaveClicked>()
                .filter { isUserCard }
                .map { SaveUserCardUseCase.Action(it.number, it.name, it.isPublic, it.cardType) }
                .compose(saveUserCardUseCase),
            observable.ofType<AddCardIntent.NumberChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(validateCardUseCase),
            observable.ofType<AddCardIntent.NumberChanged>()
                .map { IdentifyCardNumberUseCase.Action(it.number) }
                .compose(identifyCardNumberUseCase),
            observable.ofType<AddCardIntent.NumberChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
                .compose(validateCardUseCase),
            observable.ofType<AddCardIntent.NameChanged>()
                .map { ValidateCardUseCase.Action(it.number, it.name, it.isPublic) }
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
                ValidateCardUseCase.Result.Idle -> it
                is ValidateCardUseCase.Result.Failure -> it.copy(isValid = false, error = throwable, saveProgress = false)
            }
            is IdentifyCardNumberUseCase.Result -> when (this) {
                is IdentifyCardNumberUseCase.Result.Success -> it.copy(cardType = cardType)
                IdentifyCardNumberUseCase.Result.Idle -> it
                is IdentifyCardNumberUseCase.Result.Failure -> it.copy(error = throwable)
            }
            is GetCurrentUserUseCase.Result -> when (this) {
                is GetCurrentUserUseCase.Result.Success -> it.copy(
                    holderName = requireNotNull(user.toNullable()).displayName,
                    number = user.toNullable()?.card?.number ?: "",
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
            else -> throw IllegalStateException("Can not reduce user for result ${javaClass.name}")
        }
    }
}