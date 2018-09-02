package com.popalay.cardme.main

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.core.usecase.LogOutUseCase
import com.popalay.cardme.main.auth.AuthenticatorFacade
import com.popalay.cardme.main.auth.FirebasePhoneAuthenticator
import com.popalay.cardme.main.auth.GoogleAuthenticator
import com.popalay.cardme.main.usecase.AuthUseCase
import com.popalay.cardme.main.usecase.HandleAuthResultUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object MainModule {

    fun get() = module(MainModule::class.moduleName) {
        viewModel { MainViewModel(get(), get(), get { it }, get { it }, get(), get()) }
        single { LogOutUseCase() }
        single { AuthUseCase(get { it }, get()) }
        single { HandleAuthResultUseCase(get { it }, get()) }
        single<Authenticator> { AuthenticatorFacade(get { it }) }
        single { (fragment: Fragment) ->
            mapOf(
                GoogleAuthenticator::class to GoogleAuthenticator(androidContext(), fragment, get()) as Authenticator,
                FirebasePhoneAuthenticator::class to FirebasePhoneAuthenticator(get()) as Authenticator
            )
        }
    }
}