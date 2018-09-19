package com.popalay.cardme.api.ui.state

import com.popalay.cardme.api.core.usecase.UseCase
import io.reactivex.ObservableTransformer

interface Processor<I> : ObservableTransformer<I, UseCase.Result>