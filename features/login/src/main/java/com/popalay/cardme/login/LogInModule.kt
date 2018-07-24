package com.popalay.cardme.login

import android.support.v4.app.Fragment
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.login.auth.AuthenticatorFacade
import com.popalay.cardme.login.auth.FirebasePhoneAuthenticator
import com.popalay.cardme.login.auth.GoogleAuthenticator
import com.popalay.cardme.login.usecase.AuthUseCase
import com.popalay.cardme.login.usecase.HandleAuthResultUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object LogInModule {

    fun get() = module(LogInModule::class.moduleName) {
        viewModel { LogInViewModel(get(), get { it }, get { it }) }
        single { AuthUseCase(get { it }) }
        single { HandleAuthResultUseCase(get { it }) }
        single<Authenticator> { AuthenticatorFacade(get { it }) }
        single { (fragment: Fragment) ->
            mapOf(
                GoogleAuthenticator::class to GoogleAuthenticator(androidContext(), fragment, get()) as Authenticator,
                FirebasePhoneAuthenticator::class to FirebasePhoneAuthenticator(get()) as Authenticator
            )
        }
    }
}