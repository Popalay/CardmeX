package com.popalay.cardme.carddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.extensions.*
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.ProgressMaterialButton
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext.loadKoinModules
import kotlin.properties.Delegates

internal class CardDetailsFragment : Fragment(), BindableMviView<CardDetailsViewState, CardDetailsIntent> {

    private val layoutCard: CardView by bindView(R.id.layout_card)
    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)
    private val buttonSave: ProgressMaterialButton by bindView(R.id.button_save)
    private val imageUserAvatar: ImageView by bindView(R.id.image_user_avatar)
    private val textDisplayName: TextView by bindView(R.id.text_display_name)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textCardNumber: TextView by bindView(R.id.text_card_number)

    private val navigatorHolder: NavigatorHolder by inject()
    private val errorHandler: ErrorHandler by inject()
    private var state: CardDetailsViewState by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadModule()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.card_details_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            card?.run {
                textCardNumber.text = formattedNumber
                imageCardType.setImageResource(cardType.icon)
                textDisplayName.text = holder.name
                imageUserAvatar.loadImage(holder.photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
            }
            toastMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            layoutCard.isVisible = card != null && !progress
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