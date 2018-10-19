package com.popalay.cardme.authenticator

import android.app.Activity
import androidx.fragment.app.Fragment
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.authenticator.mapper.FirebaseUserToUserMapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import kotlin.reflect.KClass

object AuthenticatorModule {

    fun get() = module {
        single { FirebaseUserToUserMapper() }
        factory<Authenticator> { AuthenticatorFacade(get { it }) }
        factory { (fragment: Fragment) ->
            mapOf<KClass<*>, Authenticator>(
                GoogleAuthenticator::class to GoogleAuthenticator(
                    androidContext(),
                    fragment,
                    get()
                ),
                FirebasePhoneAuthenticator::class to FirebasePhoneAuthenticator(get())
            )
        }
    }
}