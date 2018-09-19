package com.popalay.cardme.api.state

import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.ObservableTransformer

interface Processor<I> : ObservableTransformer<I, UseCase.Result>