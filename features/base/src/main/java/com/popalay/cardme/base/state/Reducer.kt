package com.popalay.cardme.base.state

import com.popalay.cardme.base.usecase.UseCase
import io.reactivex.functions.BiFunction

typealias Reducer<S> = BiFunction<S, UseCase.Result, S>

class LambdaReducer<S>(private val block: UseCase.Result.(oldState: S) -> S) : Reducer<S> {

    override fun apply(oldState: S, result: UseCase.Result): S = block(result, oldState)
}