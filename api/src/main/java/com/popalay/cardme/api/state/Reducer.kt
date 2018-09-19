package com.popalay.cardme.api.state

import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.functions.BiFunction

typealias Reducer<S> = BiFunction<S, UseCase.Result, S>