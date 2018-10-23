package com.popalay.cardme.core.usecase

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.data.repository.DeviceRepository
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.core.extensions.formattedNumber
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class CopyCardNumberUseCase(
    private val deviceRepository: DeviceRepository
) : UseCase<CopyCardNumberUseCase.Action, CopyCardNumberUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        deviceRepository.saveToClipboard(action.card.holder.name, action.card.formattedNumber)
            .toSingleDefault(Result.Success)
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .mergeWith(
                Observable.timer(1500L, TimeUnit.MILLISECONDS)
                    .map { Result.HideMessage }
            )
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    data class Action(val card: Card) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object HideMessage : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}