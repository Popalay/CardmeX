package com.popalay.cardme.usercard.usecase

import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import com.popalay.cardme.api.data.repository.NotificationRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class NotificationListUseCase(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
) : UseCase<NotificationListUseCase.Action, NotificationListUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { _ ->
        authRepository.authState()
            .switchMap { user -> notificationRepository.getAll(user.toNullable()?.uuid ?: "") }
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val notifications: List<Notification>) : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}