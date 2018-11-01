package com.popalay.cardme.notifications;

import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import io.reactivex.rxkotlin.ofType

internal class NotificationsViewModel(
    //TODO: Use cases
) : BaseMviViewModel<NotificationsViewState, NotificationsIntent>() {

    override val initialState: NotificationsViewState = NotificationsViewState()

    override val processor: Processor<NotificationsIntent> = IntentProcessor { observable ->
        listOf(
            //TODO: Intents to Actions
        )
    }

    override val reducer: Reducer<NotificationsViewState> = LambdaReducer {
        when (this) {
            //TODO: Result to ViewState
            else -> throw IllegalStateException("Can not reduce user for result $this")
        }
    }
}