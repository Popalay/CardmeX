package com.popalay.cardme.cardlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.addcard.AddCardFragment
import com.popalay.cardme.addcard.R
import com.popalay.cardme.api.error.ErrorHandler
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
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.scopedWith
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.dsl.path.moduleName

internal class CardListFragment : Fragment(), BindableMviView<CardListViewState, CardListIntent>, OnDialogDismissed {

    private val listCards: RecyclerView by bindView(R.id.list_cards)
    private val buttonAddCard: Button by bindView(R.id.button_add_card)

    private val errorHandler: ErrorHandler by inject()

    private val addCardDialogDismissedSubject = PublishSubject.create<CardListIntent.OnAddCardDialogDismissed>()
    private val cardsAdapter = CardListAdapter()
    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.card_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<CardListViewModel>())
        scopedWith(CardListModule::class.moduleName)
        initView()
    }

    override val intents: Observable<CardListIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(CardListIntent.OnStart),
                addCardClickedIntent,
                addCardDialogDismissedSubject,
                cardClickedIntent,
                cardLongClickedIntent
            )
        )
    }

    override fun accept(viewState: CardListViewState) {
        with(viewState) {
            if (showAddCardDialog) showAddCardDialog()
            showToast(toastMessage, showToast)
            cardsAdapter.submitList(cards.map(::CardListItem))
            listCards.smoothScrollToPosition(0)
            errorHandler.accept(error)
        }
    }

    override fun onDialogDismissed(isOk: Boolean) {
        addCardDialogDismissedSubject.onNext(CardListIntent.OnAddCardDialogDismissed)
    }

    private val addCardClickedIntent
        get() = RxView.clicks(buttonAddCard)
            .applyThrottling()
            .map { CardListIntent.OnAddCardClicked }

    private val cardClickedIntent
        get() = cardsAdapter.itemClickObservable
            .applyThrottling()
            .map { CardListIntent.OnCardClicked(it.card) }

    private val cardLongClickedIntent
        get() = cardsAdapter.itemLongClickObservable
            .map { CardListIntent.OnCardLongClicked(it.card) }

    private fun showAddCardDialog() {
        if (childFragmentManager.findFragmentByTag(AddCardFragment::class.java.simpleName) == null) {
            AddCardFragment.newInstance().showNow(childFragmentManager, AddCardFragment::class.java.simpleName)
        }
    }

    private fun showToast(text: String?, show: Boolean) {
        if (show) {
            toast = toast ?: Toast.makeText(context, text, Toast.LENGTH_LONG)
            toast?.show()
        } else toast?.cancel()
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