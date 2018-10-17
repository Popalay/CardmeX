package com.popalay.cardme.usercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.addcard.AddCardFragment
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.extensions.*
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

internal class UserCardFragment : Fragment(), BindableMviView<UserCardViewState, UserCardIntent> {

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
    private val intentSubject = PublishSubject.create<UserCardIntent>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.user_card_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = UserCardNavigator(this)
        bind(getViewModel<UserCardViewModel>())
        initView()
    }

    private fun initView() {
        toolbar.inflateMenu(R.menu.user_card_menu)
        toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.action_edit -> {
                    intentSubject.onNext(UserCardIntent.OnEditClicked)
                    true
                }
                else -> false
            }
        }
    }

    override val intents: Observable<UserCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(UserCardIntent.OnStart),
                intentSubject,
                addClickedIntent.applyThrottling(),
                skipClickedIntent.applyThrottling()
            )
        )
    }

    override fun accept(viewState: UserCardViewState) {
        with(viewState) {
            user?.run {
                imageUserAvatar.loadImage(photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textDisplayName.text = displayName.value
            }
            card?.run {
                textCardNumber.text = formattedNumber
                imageCardType.setImageResource(cardType.icon)
            }
            groupNoCard.isVisible = card == null && !progress
            groupCard.isVisible = card != null && !progress
            if (progress) progressBar.show() else progressBar.hide()
            errorHandler.accept(error)
        }
    }

    private val addClickedIntent
        get() = RxView.clicks(buttonAdd)
            .map { UserCardIntent.OnAddClicked }

    private val skipClickedIntent
        get() = RxView.clicks(buttonSkip)
            .map { UserCardIntent.OnSkipClicked }
}