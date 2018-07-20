package com.popalay.cardme.login

import android.support.v4.app.Fragment
import com.popalay.cardme.login.usecase.AuthUseCase
import com.popalay.cardme.login.usecase.HandleAuthResultUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object LogInModule {

    fun get() = module(LogInModule::class.moduleName) {
        viewModel { (fragment: Fragment) -> LogInViewModel(get { parametersOf(fragment) }, get { parametersOf(fragment) }) }
        single { (fragment: Fragment) -> AuthUseCase(get { parametersOf(fragment) }) }
        single { (fragment: Fragment) -> HandleAuthResultUseCase(get { parametersOf(fragment) }) }
        single { (fragment: Fragment) -> AuthenticatorFacade(get { parametersOf(fragment) }) as Authenticator }
        single { (fragment: Fragment) ->
            mapOf(
                GoogleAuthenticator::class to GoogleAuthenticator(androidContext(), fragment) as Authenticator,
                FirebasePhoneAuthenticator::class to FirebasePhoneAuthenticator() as Authenticator
            )
        }
    }
}