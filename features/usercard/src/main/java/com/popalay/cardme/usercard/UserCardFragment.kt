package com.popalay.cardme.usercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.addcard.AddCardFragment
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.formattedNumber
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.scopedWith
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.dsl.path.moduleName

internal class UserCardFragment : Fragment(), BindableMviView<UserCardViewState, UserCardIntent>, OnDialogDismissed {

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)
    private val groupCard: Group by bindView(R.id.group_card)
    private val buttonSkip: Button by bindView(R.id.button_skip)
    private val groupNoCard: Group by bindView(R.id.group_no_card)
    private val buttonAdd: Button by bindView(R.id.button_add)
    private val layoutCard: CardView by bindView(R.id.layout_card)
    private val imageUserAvatar: ImageView by bindView(R.id.image_user_avatar)
    private val textDisplayName: TextView by bindView(R.id.text_display_name)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textCardNumber: TextView by bindView(R.id.text_card_number)

    private val errorHandler: ErrorHandler by inject()
    private val addCardDialogDismissedSubject = PublishSubject.create<UserCardIntent.OnAddCardDialogDismissed>()
    private val menuSubject = PublishSubject.create<UserCardIntent>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.user_card_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<UserCardViewModel>())
        scopedWith(UserCardModule::class.moduleName)
        initView()
    }

    private fun initView() {
        toolbar.inflateMenu(R.menu.user_card_menu)
        toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.action_edit -> {
                    menuSubject.onNext(UserCardIntent.OnEditClicked)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.user_card_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.action_edit -> {
            menuSubject.onNext(UserCardIntent.OnEditClicked)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override val intents: Observable<UserCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(UserCardIntent.OnStart),
                addCardDialogDismissedSubject,
                menuSubject.applyThrottling(),
                addClickedIntent.applyThrottling(),
                skipClickedIntent.applyThrottling()
            )
        )
    }

    override fun accept(viewState: UserCardViewState) {
        with(viewState) {
            if (showAddCardDialog) showAddCardDialog()
            user?.run {
                imageUserAvatar.loadImage(photoUrl, CircleImageTransformation())
                textDisplayName.text = displayName
            }
            card?.run {
                textCardNumber.text = formattedNumber
                val cardTypeRes = when (cardType) {
                    CardType.UNKNOWN -> return@run
                    CardType.MASTER_CARD -> R.drawable.ic_mastercard
                    CardType.VISA -> R.drawable.ic_visa
                }
                imageCardType.setImageResource(cardTypeRes)
            }
            groupNoCard.isVisible = card == null && !progress
            groupCard.isVisible = card != null && !progress
            if (progress) progressBar.show() else progressBar.hide()
            errorHandler.accept(error)
        }
    }

    override fun onDialogDismissed(isOk: Boolean) {
        addCardDialogDismissedSubject.onNext(UserCardIntent.OnAddCardDialogDismissed(isOk))
    }

    private val addClickedIntent
        get() = RxView.clicks(buttonAdd)
            .map { UserCardIntent.OnAddClicked }

    private val skipClickedIntent
        get() = RxView.clicks(buttonSkip)
            .map { UserCardIntent.OnSkipClicked }

    private fun showAddCardDialog() {
        if (childFragmentManager.findFragmentByTag(AddCardFragment::class.java.simpleName) == null) {
            AddCardFragment.newInstance(isUserCard = true).showNow(childFragmentManager, AddCardFragment::class.java.simpleName)
        }
    }
}