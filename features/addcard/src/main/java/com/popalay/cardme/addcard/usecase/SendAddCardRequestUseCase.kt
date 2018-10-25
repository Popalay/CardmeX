package com.popalay.cardme.addcard.usecase

import com.gojuno.koptional.Some
import com.popalay.cardme.api.core.model.Request
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.AuthRepository
import com.popalay.cardme.api.data.repository.RequestRepository
import com.popalay.cardme.api.ui.navigation.Router
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class SendAddCardRequestUseCase(
    private val authRepository: AuthRepository,
    private val requestRepository: RequestRepository,
    private val router: Router
) : UseCase<SendAddCardRequestUseCase.Action, SendAddCardRequestUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        authRepository.currentUser()
            .filter { it is Some }
            .map { it.component1() }
            .flatMapCompletable {
                val requestId = UUID.randomUUID().toString()
                val request = Request.AddCardRequest(requestId, it.uuid, action.user.uuid)
                requestRepository.save(request)
                    .doOnComplete { if (action.closeOnSuccess) router.navigateUp() }
            }
            .toSingleDefault(Result.Success)
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle(action.user))
            .subscribeOn(Schedulers.io())
    }

    data class Action(val user: User, val closeOnSuccess: Boolean) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        data class Idle(val user: User) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}