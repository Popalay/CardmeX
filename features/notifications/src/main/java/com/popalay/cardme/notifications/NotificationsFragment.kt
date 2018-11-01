package com.popalay.cardme.notifications;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.core.state.BindableMviView
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

internal class NotificationsFragment : Fragment(), BindableMviView<NotificationsViewState, NotificationsIntent> {

    private val errorHandler: ErrorHandler by inject()

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
            listOf<Observable<NotificationsIntent>>(
                // TODO: Intents
            )
        )
    }

    override fun accept(viewState: NotificationsViewState) {
        with(viewState) {
            //TODO: Render state
            errorHandler.accept(error)
        }
    }

    private fun initView() {
        //TODO: Init view
    }
}