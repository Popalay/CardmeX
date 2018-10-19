package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.*
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object AddCardModule {

    fun get() = module("AddCardModule") {
        viewModel { (isUserCard: Boolean) -> AddCardViewModel(isUserCard, get(), get(), get(), get(), get(), get(), get(), get(), get()) }
        single { SaveCardUseCase(get { it }, get { it }) }
        single { SaveUserCardUseCase(get { it }, get { it }, get { it }) }
        single { ValidateCardUseCase() }
        single { IdentifyCardNumberUseCase() }
        single { UserListUseCase(get { it }) }
        single { SendAddCardRequestUseCase(get { it }, get { it }) }
    }
}