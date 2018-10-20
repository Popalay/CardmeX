package com.popalay.cardme.main.usecase

import com.popalay.cardme.api.auth.AuthResultFactory
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import com.popalay.cardme.api.data.repository.NotificationRepository
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class HandleAuthResultUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val authResultFactory: AuthResultFactory,
    private val notificationRepository: NotificationRepository
) : UseCase<HandleAuthResultUseCase.Action, HandleAuthResultUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        authRepository.handleResult(authResultFactory.build(action.success, action.requestCode, action.data))
            .flatMapPublisher { user -> user.toNullable()?.let { userRepository.get(it.uuid) } ?: Flowable.just(user) }
            .flatMapCompletable { notificationRepository.syncToken() }
            .toSingleDefault(AuthUseCase.Result.Success)
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val success: Boolean, val requestCode: Int, val data: android.content.Intent) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}