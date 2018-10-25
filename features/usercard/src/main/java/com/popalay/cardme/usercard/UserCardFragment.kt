package com.popalay.cardme.usercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.extensions.*
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext.loadKoinModules
import kotlin.properties.Delegates

internal class UserCardFragment : Fragment(), BindableMviView<UserCardViewState, UserCardIntent> {

    private val layoutCard: CardView by bindView(R.id.layout_card)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)
    private val groupCard: Group by bindView(R.id.group_card)
    private val buttonSkip: Button by bindView(R.id.button_skip)
    private val groupNoCard: Group by bindView(R.id.group_no_card)
    private val buttonAdd: Button by bindView(R.id.button_add)
    private val imageUserAvatar: ImageView by bindView(R.id.image_user_avatar)
    private val textDisplayName: TextView by bindView(R.id.text_display_name)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textCardNumber: TextView by bindView(R.id.text_card_number)

    private val errorHandler: ErrorHandler by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private var state: UserCardViewState by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadModule()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.user_card_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = UserCardNavigator(this)
        bind(getViewModel<UserCardViewModel> { parametersOf(this) })
        initView()
    }

    private fun initView() {
        toolbar.inflateMenu(R.menu.user_card_menu)
    }

    override val intents: Observable<UserCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(UserCardIntent.OnStart),
                addClickedIntent,
                skipClickedIntent,
                editClickedIntent,
                shareClickedIntent,
                cardClickedIntent
            )
        )
    }

    override fun accept(viewState: UserCardViewState) {
        state = viewState
        with(viewState) {
            user?.run {
                imageUserAvatar.loadImage(photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textDisplayName.text = displayName.value
            }
            card?.run {
                textCardNumber.text = formattedNumber
                imageCardType.setImageResource(cardType.icon)
            }
            toastMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            groupNoCard.isVisible = card == null && !progress
            groupCard.isVisible = card != null && !progress
            if (progress) progressBar.show() else progressBar.hide()
            errorHandler.accept(error)
        }
    }

    private val addClickedIntent
        get() = RxView.clicks(buttonAdd)
            .applyThrottling()
            .map { UserCardIntent.OnAddClicked }

    private val skipClickedIntent
        get() = RxView.clicks(buttonSkip)
            .applyThrottling()
            .map { UserCardIntent.OnSkipClicked }

    private val cardClickedIntent
        get() = RxView.clicks(layoutCard)
            .applyThrottling()
            .map { UserCardIntent.OnCardClicked(requireNotNull(state.card)) }

    private val editClickedIntent
        get() = RxMenuItem.clicks(toolbar.menu.findItem(R.id.action_edit))
            .applyThrottling()
            .map { UserCardIntent.OnEditClicked }

    private val shareClickedIntent
        get() = RxMenuItem.clicks(toolbar.menu.findItem(R.id.action_share))
            .applyThrottling()
            .map { UserCardIntent.OnShareClicked(requireNotNull(state.card)) }
}