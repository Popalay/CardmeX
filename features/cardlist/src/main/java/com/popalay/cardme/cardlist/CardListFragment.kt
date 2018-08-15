package com.popalay.cardme.cardlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.addcard.AddCardFragment
import com.popalay.cardme.addcard.R
import com.popalay.cardme.cardlist.adapter.CardListAdapter
import com.popalay.cardme.cardlist.model.CardListItem
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.px
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.getViewModel

internal class CardListFragment : Fragment(), BindableMviView<CardListViewState, CardListIntent>, OnDialogDismissed {

    private val listCards: RecyclerView by bindView(R.id.list_cards)
    private val buttonAddCard: Button by bindView(R.id.button_add_card)

    private val addCardDialogDismissedSubject = PublishSubject.create<CardListIntent.OnAddCardDialogDismissed>()
    private val cardsAdapter = CardListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.card_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<CardListViewModel>())
        initView()
    }

    override val intents: Observable<CardListIntent> = Observable.defer {
        Observable.merge(
            Observable.just(CardListIntent.OnStart),
            addCardClickedIntent,
            addCardDialogDismissedSubject
        )
    }

    override fun accept(viewState: CardListViewState) {
        with(viewState) {
            if (showAddCardDialog) showAddCardDialog()
            cardsAdapter.submitList(cards.map(::CardListItem))
            listCards.smoothScrollToPosition(0)
        }
    }

    override fun onDialogDismissed() {
        addCardDialogDismissedSubject.onNext(CardListIntent.OnAddCardDialogDismissed)
    }

    private val addCardClickedIntent
        get() = RxView.clicks(buttonAddCard)
            .applyThrottling()
            .map { CardListIntent.OnAddCardClicked }

    private fun showAddCardDialog() {
        if (childFragmentManager.findFragmentByTag(AddCardFragment::class.java.simpleName) == null) {
            AddCardFragment().show(childFragmentManager, AddCardFragment::class.java.simpleName)
        }
    }

    private fun initView() {
        listCards.apply {
            setHasFixedSize(true)
            adapter = cardsAdapter
            addItemDecoration(SpacingItemDecoration(16.px))
            addItemDecoration(SpacingItemDecoration(onSides = false, betweenItems = true, dividerSize = 8.px))
        }
    }
}