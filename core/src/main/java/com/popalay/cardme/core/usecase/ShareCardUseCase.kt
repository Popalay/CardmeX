package com.popalay.cardme.core.usecase

import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.popalay.cardme.api.core.usecase.UseCase
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.core.extensions.createDynamicLink
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

class ShareCardUseCase(
    private val fragment: Fragment,
    private val cardRepository: CardRepository
) : UseCase<ShareCardUseCase.Action, ShareCardUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream.switchMap { action ->
        cardRepository.get(action.cardId)
            .firstElement()
            .flatMapSingle {
                Uri.Builder()
                    .scheme("https")
                    .authority("mecard.page.link")
                    .appendPath("card")
                    .appendPath(it.toNullable()?.id)
                    .build()
                    .createDynamicLink()
            }
            .doOnSuccess {
                val intent = ShareCompat.IntentBuilder.from(fragment.requireActivity())
                    .setChooserTitle("Share with..")
                    .setType("text/plain")
                    .setText("Hey, check out my card: $it")
                    .createChooserIntent()
                if (intent.resolveActivity(fragment.requireActivity().packageManager) != null) {
                    fragment.startActivity(intent)
                }
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