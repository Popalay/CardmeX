package com.popalay.cardme.carddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.CreditCardView
import com.popalay.cardme.core.widget.ProgressMaterialButton
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

internal class CardDetailsFragment : Fragment(), BindableMviView<CardDetailsViewState, CardDetailsIntent> {

    private val groupCardFields: Group by bindView(R.id.group_card_fields)
    private val layoutCard: CreditCardView by bindView(R.id.layout_card)
    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)
    private val buttonSave: ProgressMaterialButton by bindView(R.id.button_save)

    private val navigatorHolder: NavigatorHolder by inject()
    private val errorHandler: ErrorHandler by inject()
    private var state: CardDetailsViewState by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.card_details_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadModule()
        navigatorHolder.navigator = CardDetailsNavigator(this)
        val args = CardDetailsFragmentArgs.fromBundle(arguments)
        bind(getViewModel<CardDetailsViewModel> { parametersOf(args.cardId) })
        initView()
    }

    private fun initView() {
    }

    override val intents: Observable<CardDetailsIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(CardDetailsIntent.OnStart),
                addClickedIntent,
                cardClickedIntent
            )
        )
    }

    override fun accept(viewState: CardDetailsViewState) {
        state = viewState
        with(viewState) {
            layoutCard.card = card
            toastMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            groupCardFields.isVisible = card != null && !progress
            layoutCard.isVisible = !progress
            buttonSave.isVisible = card != null && !progress
            buttonSave.isProgress = saveProgress
            if (progress) progressBar.show() else progressBar.hide()
            errorHandler.accept(error)
        }
    }

    private val cardClickedIntent
        get() = RxView.clicks(layoutCard)
            .applyThrottling()
            .map { CardDetailsIntent.OnCardClicked(requireNotNull(state.card)) }

    private val addClickedIntent
        get() = RxView.clicks(buttonSave)
            .applyThrottling()
            .map { CardDetailsIntent.OnAddClicked(requireNotNull(state.card)) }
}