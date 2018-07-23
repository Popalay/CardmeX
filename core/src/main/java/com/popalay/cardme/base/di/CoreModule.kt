package com.popalay.cardme.base.di

import com.gojuno.koptional.Optional
import com.google.firebase.auth.FirebaseUser
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.User
import com.popalay.cardme.base.error.DefaultErrorHandler
import com.popalay.cardme.base.mapper.FirebaseUserToUserMapper
import org.koin.dsl.module.module

object CoreModule {

    fun get() = module {
        single { DefaultErrorHandler() as ErrorHandler }
        single { FirebaseUserToUserMapper() as Mapper<FirebaseUser?, Optional<User>> }
    }
}