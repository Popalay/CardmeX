package com.popalay.cardme.usercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.state.BindableMviView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.scopedWith
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.dsl.path.moduleName

internal class UserCardFragment : Fragment(), BindableMviView<UserCardViewState, UserCardIntent> {

    private val groupCard: Group by bindView(R.id.group_card)
    private val buttonSkip: Button by bindView(R.id.button_skip)
    private val groupNoCard: Group by bindView(R.id.group_no_card)
    private val buttonAdd: Button by bindView(R.id.button_add)
    private val textNoCardMessage: TextView by bindView(R.id.text_no_card_message)
    private val layoutCard: CardView by bindView(R.id.layout_card)
    private val buttonEdit: FloatingActionButton by bindView(R.id.button_edit)
    private val imageUserAvatar: ImageView by bindView(R.id.image_user_avatar)
    private val textDisplayName: TextView by bindView(R.id.text_display_name)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textCardNumber: TextView by bindView(R.id.text_card_number)

    private val errorHandler: ErrorHandler by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.user_card_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<UserCardViewModel>())
        scopedWith(UserCardModule::class.moduleName)
    }

    override val intents: Observable<UserCardIntent> = Observable.defer {
        Observable.merge(
            Observable.just(UserCardIntent.OnStart),
            editClickedIntent,
            addClickedIntent,
            skipClickedIntent
        )
    }

    override fun accept(viewState: UserCardViewState) {
        with(viewState) {
            user?.run {
                Picasso.get().load(user.photoUrl).into(imageUserAvatar)
                textDisplayName.text = displayName
            }
            card?.run {
                textCardNumber.text = number
                val cardTypeRes = when (cardType) {
                    CardType.UNKNOWN -> R.drawable.ic_credit_card
                    CardType.MASTER_CARD -> R.drawable.ic_mastercard
                    CardType.VISA -> R.drawable.ic_visa
                }
                imageCardType.setImageResource(cardTypeRes)
            }
            groupNoCard.isVisible = card == null
            groupCard.isVisible = card != null
            errorHandler.accept(error)
        }
    }

    private val editClickedIntent
        get() = RxView.clicks(buttonEdit)
            .applyThrottling()
            .map { UserCardIntent.OnEditClicked }

    private val addClickedIntent
        get() = RxView.clicks(buttonAdd)
            .applyThrottling()
            .map { UserCardIntent.OnAddClicked }

    private val skipClickedIntent
        get() = RxView.clicks(buttonSkip)
            .applyThrottling()
            .map { UserCardIntent.OnSkipClicked }
}