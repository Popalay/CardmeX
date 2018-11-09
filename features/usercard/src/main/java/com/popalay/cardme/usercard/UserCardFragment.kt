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
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.contains
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.jakewharton.rxbinding2.view.clicks
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.*
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.CharacterWrapTextView
import com.popalay.cardme.usercard.adapter.NotificationListAdapter
import com.popalay.cardme.usercard.model.NotificationListItem
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

internal class UserCardFragment : Fragment(), BindableMviView<UserCardViewState, UserCardIntent> {

    private val labelNotifications: TextView by bindView(R.id.label_notifications)
    private val listNotifications: RecyclerView by bindView(R.id.list_notifications)
    private val cardNotifications: MaterialCardView by bindView(R.id.card_notifications)
    private val buttonAdd: Button by bindView(R.id.button_add)
    private val motionLayout: MotionLayout by bindView(R.id.motion_layout)
    private val imageUserAvatar: ImageView by bindView(R.id.image_user_avatar)
    private val textDisplayName: TextView by bindView(R.id.text_display_name)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textCardNumber: CharacterWrapTextView by bindView(R.id.text_card_number)
    private val layoutCard: CardView by bindView(R.id.layout_card)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)

    private val notificationsAdapter = NotificationListAdapter()

    private val errorHandler: ErrorHandler by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private var state: UserCardViewState by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.user_card_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = UserCardNavigator(this)
        loadModule()
        bind(getViewModel<UserCardViewModel> { parametersOf(this) })
        initView()
    }

    private fun initView() {
        NavigationUI.setupWithNavController(toolbar, findNavController())
        toolbar.inflateMenu(R.menu.user_card_menu)
        listNotifications.apply {
            adapter = notificationsAdapter
            addItemDecoration(SpacingItemDecoration(8.px))
            addItemDecoration(SpacingItemDecoration(16.px, betweenItems = true, onSides = false))
        }
    }

    override val intents: Observable<UserCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(UserCardIntent.OnStart),
                addClickedIntent,
                editClickedIntent,
                shareClickedIntent,
                cardClickedIntent
            )
        )
    }

    override fun accept(viewState: UserCardViewState) {
        state = viewState
        with(viewState) {
            textCardNumber.text = card?.formattedNumber
            imageCardType.setImageResource((card?.cardType ?: CardType.MASTER_CARD).icon)

            user?.run {
                imageUserAvatar.loadImage(photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textDisplayName.text = displayName.value
            }

            toastMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            toolbar.menu.run {
                findItem(R.id.action_edit).isVisible = card != null
                findItem(R.id.action_share).isVisible = card != null
                findItem(R.id.action_add).isVisible = card == null
            }
            notificationsAdapter.submitList(notifications.map(::NotificationListItem))
            buttonAdd.isVisibleForMotion(motionLayout, card == null)
            progressBar.isVisibleForMotion(motionLayout, progress)
            motionLayout.setTransition(
                R.id.user_card_expanded,
                if (notifications.isNotEmpty()) R.id.user_card_collapsed else R.id.user_card_expanded
            )
            cardNotifications.isVisibleForMotion(motionLayout, notifications.isNotEmpty())
            labelNotifications.isVisibleForMotion(motionLayout, notifications.isNotEmpty())
            errorHandler.accept(error)
        }
    }

    private fun View.isVisibleForMotion(motionLayout: MotionLayout, isVisible: Boolean) {
        if (isVisible && this !in motionLayout) {
            motionLayout.addView(this)
        } else if (!isVisible && this in motionLayout) {
            motionLayout.removeView(this)
        }
    }

    private val RecyclerView.isAllItemsVisible: Boolean
        get() = (layoutManager as? LinearLayoutManager)?.let {
            it.findFirstCompletelyVisibleItemPosition() <= 0 &&
                    it.findLastCompletelyVisibleItemPosition() == listNotifications.childCount - 1
        } ?: false

    private val cardClickedIntent
        get() = layoutCard.clicks()
            .applyThrottling()
            .map { UserCardIntent.OnCardClicked(state.card) }

    private val addClickedIntent
        get() = Observable.merge(
            toolbar.menu.findItem(R.id.action_add).clicks()
                .applyThrottling(),
            buttonAdd.clicks()
                .applyThrottling()
        ).map { UserCardIntent.OnAddClicked }

    private val editClickedIntent
        get() = toolbar.menu.findItem(R.id.action_edit).clicks()
            .applyThrottling()
            .map { UserCardIntent.OnEditClicked }

    private val shareClickedIntent
        get() = toolbar.menu.findItem(R.id.action_share).clicks()
            .applyThrottling()
            .map { UserCardIntent.OnShareClicked(requireNotNull(state.card)) }
}