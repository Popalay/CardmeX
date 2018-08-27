package com.popalay.cardme.usercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

internal class UserCardFragment : Fragment(), BindableMviView<UserCardViewState, UserCardIntent> {

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
        //bind(getViewModel<UserCardViewModel>())
        initView()
    }

    override val intents: Observable<UserCardIntent> = Observable.defer {
        editClickedIntent
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
            errorHandler.accept(error)
        }
    }

    private val editClickedIntent
        get() = RxView.clicks(buttonEdit)
            .applyThrottling()
            .map { UserCardIntent.OnEditClicked }

    private fun initView() {

    }
}