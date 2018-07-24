package com.popalay.cardme.core.usecase

import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource

class NavigationUseCase : UseCase<NavigationUseCase.Action, NavigationUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap {
        Observable.just(Result)
    }

    object Action : UseCase.Action

    object Result : UseCase.Result
}