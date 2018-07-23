package com.popalay.cardme.core.state

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}