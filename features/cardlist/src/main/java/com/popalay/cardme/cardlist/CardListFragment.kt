package com.popalay.cardme.cardlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.popalay.cardme.addcard.R
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.cardactions.CardActionsFragment
import com.popalay.cardme.cardlist.adapter.CardListAdapter
import com.popalay.cardme.cardlist.model.CardListItem
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.px
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

internal class CardListFragment : Fragment(), BindableMviView<CardListViewState, CardListIntent>, OnDialogDismissed {

    private val listCards: RecyclerView by bindView(R.id.list_cards)

    private val errorHandler: ErrorHandler by inject()

    private val intentSubject = PublishSubject.create<CardListIntent>()
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
            listOf(
                Observable.just(CardListIntent.OnStart),
                intentSubject,
                cardClickedIntent,
                cardLongClickedIntent
            )
        )
    }

    override fun accept(viewState: CardListViewState) {
        with(viewState) {
            selectedCard?.let { showCardActionsDialog(it) }
            toastMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            cardsAdapter.submitList(cards.map(::CardListItem))
            if (cardsAdapter.itemCount < cards.size) listCards.smoothScrollToPosition(0)
            errorHandler.accept(error)
        }
    }

    override fun onDialogDismissed() {
        intentSubject.onNext(CardListIntent.OnCardActionsDialogDismissed)
    }

    private val cardClickedIntent
        get() = cardsAdapter.itemClickObservable
            .map { CardListIntent.OnCardClicked(it.card) }

    private val cardLongClickedIntent
        get() = cardsAdapter.itemLongClickObservable
            .map { CardListIntent.OnCardLongClicked(it.card) }

    private fun showCardActionsDialog(card: Card) {
        if (childFragmentManager.findFragmentByTag(CardActionsFragment::class.java.simpleName) == null) {
            CardActionsFragment.newInstance(card).showNow(childFragmentManager, CardActionsFragment::class.java.simpleName)
        }
    }

    private fun initView() {
        listCards.apply {
            setHasFixedSize(true)
            adapter = cardsAdapter
            addItemDecoration(SpacingItemDecoration(8.px, betweenItems = true))
        }
    }
}