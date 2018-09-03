package com.popalay.cardme.cardactions

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.core.widget.OnDialogDismissed
import com.popalay.cardme.core.widget.RoundedBottomSheetDialogFragment
import org.koin.android.ext.android.inject

class CardActionsFragment : RoundedBottomSheetDialogFragment()/*, BindableMviView<AddCardViewState, AddCardIntent>*/ {

    companion object {

        private const val ARG_CARD_ID= "ARG_CARD_ID"

        fun newInstance(card: Card) = CardActionsFragment().apply {
            arguments = bundleOf(ARG_CARD_ID to card.id)
        }
    }

    private val errorHandler: ErrorHandler by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.card_actions_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bind(getViewModel<AddCardViewModel> { parametersOf(arguments?.getBoolean(ARG_IS_USER_CARD) ?: false) })
        //scopedWith(AddCardModule::class.moduleName)
        initView()
    }

/*    override val intents: Observable<AddCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(AddCardIntent.OnStart),
                saveClickedIntent,
                nameChangedIntent,
                numberChangedIntent
            )
        )
    }

    override fun accept(viewState: AddCardViewState) {
        with(viewState) {
        }
    }*/

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        (parentFragment as? OnDialogDismissed)?.onDialogDismissed()
    }

    private fun initView() {

    }
}