package com.popalay.cardme.addcard

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import com.popalay.cardme.core.widget.ProgressMaterialButton
import com.popalay.cardme.core.widget.RoundedBottomSheetDialogFragment
import io.reactivex.Observable
import org.koin.androidx.scope.ext.android.scopedWith
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.dsl.path.moduleName

class AddCardFragment : RoundedBottomSheetDialogFragment(), BindableMviView<AddCardViewState, AddCardIntent> {

    private val inputNumber: EditText by bindView(R.id.input_number)
    private val buttonCamera: ImageButton by bindView(R.id.button_camera)
    private val buttonSave: ProgressMaterialButton by bindView(R.id.button_save)
    private val inputName: EditText by bindView(R.id.input_name)
    private val checkPublic: CheckBox by bindView(R.id.check_public)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_card_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<AddCardViewModel>())
        scopedWith(AddCardModule::class.moduleName)
    }

    override val intents: Observable<AddCardIntent> = Observable.defer {
        saveClickedIntent
    }

    override fun accept(viewState: AddCardViewState) {
        with(viewState) {
            buttonSave.isProgress = progress
            if (saved) dismissAllowingStateLoss()
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        (parentFragment as? OnDialogDismissed)?.onDialogDismissed()
    }

    private val saveClickedIntent
        get() = RxView.clicks(buttonSave)
            .applyThrottling()
            .map {
                AddCardIntent.SaveClicked(
                    inputNumber.text.toString(),
                    inputName.text.toString(),
                    checkPublic.isChecked
                )
            }
}