package com.popalay.cardme.notifications;

import com.popalay.cardme.api.ui.state.IntentProcessor
import com.popalay.cardme.api.ui.state.LambdaReducer
import com.popalay.cardme.api.ui.state.Processor
import com.popalay.cardme.api.ui.state.Reducer
import com.popalay.cardme.core.state.BaseMviViewModel
import com.popalay.cardme.notifications.usecase.NotificationListUseCase
import io.reactivex.rxkotlin.ofType

internal class NotificationsViewModel(
    private val notificationListUseCase: NotificationListUseCase
) : BaseMviViewModel<NotificationsViewState, NotificationsIntent>() {

    override val initialState: NotificationsViewState = NotificationsViewState()

    override val processor: Processor<NotificationsIntent> = IntentProcessor { observable ->
        listOf(
            observable.ofType<NotificationsIntent.OnStart>()
                .map { NotificationListUseCase.Action }
                .compose(notificationListUseCase)
        )
    }

    override val reducer: Reducer<NotificationsViewState> = LambdaReducer {
        when (this) {
            is NotificationListUseCase.Result -> when (this) {
                is NotificationListUseCase.Result.Success -> it.copy(notifications = notifications, progress = false)
                NotificationListUseCase.Result.Idle -> it.copy(progress = true)
                is NotificationListUseCase.Result.Failure -> it.copy(error = throwable, progress = false)
            }
            else -> throw IllegalStateException("Can not reduce user for result $this")
        }
    }
}