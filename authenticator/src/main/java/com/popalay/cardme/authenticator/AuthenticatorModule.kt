package com.popalay.cardme.authenticator

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.popalay.cardme.api.auth.AuthCredentialsFactory
import com.popalay.cardme.api.auth.AuthResultFactory
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.authenticator.mapper.FirebaseUserToUserMapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import kotlin.reflect.KClass

object AuthenticatorModule {

    fun get() = module {
        single { FirebaseUserToUserMapper() }
        single { FirebaseAuth.getInstance() }
        single<AuthResultFactory> { GoogleAuthResultFactory() }
        single<AuthCredentialsFactory> { GoolgeAuthCredentialsFactory() }
        factory<Authenticator> { AuthenticatorFacade(get { it }, get { it }, get { it }) }
        factory { parameters ->
            mapOf<KClass<*>, Authenticator>(
                GoogleAuthenticator::class to GoogleAuthenticator(
                    androidContext(),
                    parameters.values.firstOrNull { it is Fragment } as Fragment?,
                    get(),
                    get()
                ),
                FirebasePhoneAuthenticator::class to FirebasePhoneAuthenticator(get(), get())
            )
        }
    }
}