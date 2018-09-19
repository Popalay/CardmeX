package com.popalay.cardme.cardactions.usecase

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.core.extensions.formattedNumber
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

internal class ShareCardUseCase(
    private val context: Context,
    private val cardRepository: CardRepository
) : UseCase<ShareCardUseCase.Action, ShareCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        cardRepository.get(action.cardId)
            .doOnNext {
                    val sendIntent: Intent = Intent().apply {
                        this.action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "${it.holder.name}: ${it.formattedNumber}")
                        type = "text/plain"
                    }
                    ContextCompat.startActivity(context, sendIntent, null)
            }
            .map { Result.Success }
            .cast(Result::class.java)
            .onErrorReturn(Result::Failure)
            .toObservable()
            .startWith(Result.Idle)
            .subscribeOn(Schedulers.io())
    }

    data class Action(val cardId: String) : UseCase.Action

    sealed class Result : UseCase.Result {
        object Success : Result()
        object Idle : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}