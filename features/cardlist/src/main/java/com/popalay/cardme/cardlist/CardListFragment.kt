package com.popalay.cardme.cardlist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.addcard.AddCardFragment
import com.popalay.cardme.addcard.R
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.viewmodel.ext.android.getViewModel


internal class CardListFragment : Fragment(), BindableMviView<CardListViewState, CardListIntent>, OnDialogDismissed {

    private val listCards: ListView by bindView(R.id.list_cards)
    private val buttonAddCard: Button by bindView(R.id.button_add_card)

    private val addCardDialogDismissedSubject = PublishSubject.create<CardListIntent.OnAddCardDialogDismissed>().toSerialized()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.card_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<CardListViewModel>())
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
            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                cards.map { "${it.number} ${it.holder.name}" }
            )
            listCards.adapter = adapter
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
}