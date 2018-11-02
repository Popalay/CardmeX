package com.popalay.cardme.notifications;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.px
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.notifications.adapter.NotificationListAdapter
import com.popalay.cardme.notifications.model.NotificationListItem
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

internal class NotificationsFragment : Fragment(), BindableMviView<NotificationsViewState, NotificationsIntent> {

    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)
    private val listNotifications: RecyclerView by bindView(R.id.list_notifications)

    private val errorHandler: ErrorHandler by inject()
    private val notificationsAdapter = NotificationListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.notifications_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadModule()
        bind(getViewModel<NotificationsViewModel>())
        initView()
    }

    override val intents: Observable<NotificationsIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(NotificationsIntent.OnStart)
            )
        )
    }

    override fun accept(viewState: NotificationsViewState) {
        with(viewState) {
            if (progress) progressBar.show() else progressBar.hide()
            notificationsAdapter.submitList(notifications.map(::NotificationListItem))
            errorHandler.accept(error)
        }
    }

    private fun initView() {
        listNotifications.apply {
            adapter = notificationsAdapter
            addItemDecoration(SpacingItemDecoration(8.px, betweenItems = true))
        }
    }
}