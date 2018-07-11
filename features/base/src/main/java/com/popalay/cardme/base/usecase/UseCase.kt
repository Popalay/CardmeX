package com.popalay.cardme.base.usecase

import io.reactivex.ObservableTransformer

/**
 * Created by Denys Nykyforov on 30.12.2017
 * Copyright (c) 2017. All right reserved
 */
interface UseCase<A : UseCase.Action, R : UseCase.Result> : ObservableTransformer<A, R> {

    interface Action

    interface Result
}