package com.popalay.cardme.login

import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.base.ErrorHandler
import com.popalay.cardme.base.extensions.bindView
import com.popalay.cardme.base.state.MviView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.koin.android.architecture.ext.getViewModel
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class LogInFragment : Fragment(), MviView<LogInViewState, LogInIntent> {

    private val textPhoneNumber: TextInputEditText by bindView(R.id.text_phone_number)
    private val buttonGetStarted: MaterialButton by bindView(R.id.button_get_started)

    private val disposables = CompositeDisposable()
    private val errorHandler: ErrorHandler by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.log_in_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = getViewModel<LogInViewModel>()

        disposables += viewModel.states
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this, errorHandler)

        disposables += intents
            .delaySubscription(viewModel.states.observeOn(AndroidSchedulers.mainThread()))
            .subscribe(viewModel, errorHandler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    override val intents: Observable<LogInIntent> = Observable.defer {
        Observable.merge(
            phoneNumberChangedIntent,
            getStatedClickedIntent
        )
    }

    override fun accept(viewState: LogInViewState) {
        buttonGetStarted.isEnabled = viewState.canStart
        Runtime.getRuntime().availableProcessors()
        errorHandler.accept(viewState.error)
    }

    private val phoneNumberChangedIntent
        get() = RxTextView.textChanges(textPhoneNumber)
            .map { LogInIntent.PhoneNumberChanged(it.toString()) }

    private val getStatedClickedIntent
        get() = RxView.clicks(buttonGetStarted)
            .throttleLast(500L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { LogInIntent.GetStartedClicked(textPhoneNumber.text.toString()) }
}