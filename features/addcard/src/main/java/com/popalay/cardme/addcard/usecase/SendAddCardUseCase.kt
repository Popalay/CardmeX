package com.popalay.cardme.addcard.usecase

import com.gojuno.koptional.Some
import com.popalay.cardme.api.core.model.Request
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.RequestRepository
import com.popalay.cardme.api.data.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class SendAddCardUseCase(
    private val userRepository: UserRepository,
    private val requestRepository: RequestRepository
) : UseCase<SendAddCardUseCase.Action, SendAddCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        userRepository.getCurrentUser()
            .filter { it is Some }
            .map { it.component1() }
            .flatMapCompletable {
                val requestId = UUID.randomUUID().toString()
                val request = Request.AddCardRequest(requestId, it.uuid, action.user.uuid)
                requestRepository.save(request)
            }
            .toSingleDefault(Result.Success)
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val user: User) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}