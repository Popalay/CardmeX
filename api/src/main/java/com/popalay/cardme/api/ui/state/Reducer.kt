package com.popalay.cardme.api.ui.state

import com.popalay.cardme.api.core.usecase.UseCase
import io.reactivex.functions.BiFunction

typealias Reducer<S> = BiFunction<S, UseCase.Result, S>