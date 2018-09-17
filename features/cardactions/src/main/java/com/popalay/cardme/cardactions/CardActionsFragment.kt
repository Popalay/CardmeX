package com.popalay.cardme.cardactions

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import com.popalay.cardme.core.widget.RoundedBottomSheetDialogFragment
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class CardActionsFragment : RoundedBottomSheetDialogFragment(), BindableMviView<CardActionsViewState, CardActionsIntent> {

    companion object {

        private const val ARG_CARD_ID = "ARG_CARD_ID"

        fun newInstance(card: Card) = CardActionsFragment().apply {
            arguments = bundleOf(ARG_CARD_ID to card.id)
        }
    }

    private val buttonRemove: TextView by bindView(R.id.button_remove)
    private val buttonShare: TextView by bindView(R.id.button_share)

    private val errorHandler: ErrorHandler by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.card_actions_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<CardActionsViewModel> { parametersOf(arguments?.getString(ARG_CARD_ID) ?: "") })
    }

    override val intents: Observable<CardActionsIntent> = Observable.defer {
        Observable.merge(
            listOf(
                removeClickedIntent,
                shareClickedIntent
            )
        )
    }

    override fun accept(viewState: CardActionsViewState) {
        with(viewState) {
            if (completed) dismissAllowingStateLoss()
            errorHandler.accept(error)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        (parentFragment as? OnDialogDismissed)?.onDialogDismissed()
    }

    private val removeClickedIntent
        get() = RxView.clicks(buttonRemove)
            .applyThrottling()
            .map { CardActionsIntent.OnRemoveClicked }

    private val shareClickedIntent
        get() = RxView.clicks(buttonShare)
            .applyThrottling()
            .map { CardActionsIntent.OnShareClicked }
}