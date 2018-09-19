package com.popalay.cardme.core.usecase

import com.popalay.cardme.api.ui.state.Intent
import com.popalay.cardme.api.core.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource

class SpecificIntentUseCase : UseCase<SpecificIntentUseCase.Action, SpecificIntentUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.map { Result(it.intent) }

    data class Action(val intent: Intent) : UseCase.Action

    data class Result(val intent: Intent) : UseCase.Result
}