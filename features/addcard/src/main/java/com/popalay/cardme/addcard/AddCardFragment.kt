package com.popalay.cardme.addcard

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.transition.TransitionManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import com.popalay.cardme.core.widget.ProgressMaterialButton
import com.popalay.cardme.core.widget.RoundedBottomSheetDialogFragment
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.scopedWith
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.path.moduleName
import kotlin.properties.Delegates

class AddCardFragment : RoundedBottomSheetDialogFragment(), BindableMviView<AddCardViewState, AddCardIntent> {

    companion object {

        private const val ARG_IS_USER_CARD = "ARG_IS_USER_CARD"

        fun newInstance(isUserCard: Boolean = false) = AddCardFragment().apply {
            arguments = bundleOf(ARG_IS_USER_CARD to isUserCard)
        }
    }

    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val inputNumber: EditText by bindView(R.id.input_number)
    private val buttonCamera: ImageButton by bindView(R.id.button_camera)
    private val buttonSave: ProgressMaterialButton by bindView(R.id.button_save)
    private val inputName: EditText by bindView(R.id.input_name)
    private val checkPublic: CheckBox by bindView(R.id.check_public)

    private val errorHandler: ErrorHandler by inject()

    private var state: AddCardViewState by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.add_card_fragment, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<AddCardViewModel> { parametersOf(arguments?.getBoolean(ARG_IS_USER_CARD) ?: false) })
        scopedWith(AddCardModule::class.moduleName)
    }

    override val intents: Observable<AddCardIntent> = Observable.defer {
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
        state = viewState
        with(viewState) {
            buttonSave.isEnabled = isValid
            buttonSave.isProgress = progress
            checkPublic.isChecked = isPublic
            checkPublic.isEnabled = isPublicEditable
            inputName.isEnabled = isHolderNameEditable
            if (holderName.isNotBlank()) inputName.setText(holderName)
            if (saved) dismissAllowingStateLoss()
            val cardTypeRes = when (cardType) {
                CardType.UNKNOWN -> R.drawable.ic_credit_card
                CardType.MASTER_CARD -> R.drawable.ic_mastercard
                CardType.VISA -> R.drawable.ic_visa
            }
            TransitionManager.beginDelayedTransition(view as ViewGroup)
            imageCardType.setImageResource(cardTypeRes)
            errorHandler.accept(error)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        (parentFragment as? OnDialogDismissed)?.onDialogDismissed(state.saved)
    }

    private val saveClickedIntent
        get() = RxView.clicks(buttonSave)
            .applyThrottling()
            .map {
                AddCardIntent.SaveClicked(
                    inputNumber.text.toString(),
                    inputName.text.toString(),
                    checkPublic.isChecked,
                    state.cardType
                )
            }

    private val nameChangedIntent
        get() = RxTextView.afterTextChangeEvents(inputName)
            .skipInitialValue()
            .map {
                AddCardIntent.NameChanged(
                    inputNumber.text.toString(),
                    inputName.text.toString(),
                    checkPublic.isChecked
                )
            }

    private val numberChangedIntent
        get() = RxTextView.afterTextChangeEvents(inputNumber)
            .skipInitialValue()
            .map {
                AddCardIntent.NumberChanged(
                    inputNumber.text.toString(),
                    inputName.text.toString(),
                    checkPublic.isChecked
                )
            }
}