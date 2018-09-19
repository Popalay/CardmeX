package com.popalay.cardme.api.ui.state

import com.popalay.cardme.api.core.usecase.UseCase

class LambdaReducer<S>(private val block: UseCase.Result.(oldState: S) -> S) : Reducer<S> {

    override fun apply(oldState: S, result: UseCase.Result): S = block(result, oldState)
}