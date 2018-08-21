package com.popalay.cardme.core.error

import android.util.Log
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import java.io.IOException
import java.net.SocketException

class DefaultRxErrorHandler : Consumer<Throwable> {

    override fun accept(e: Throwable?) {
        val exception = when (e) {
            is UndeliverableException -> e.cause
        // fine, irrelevant network problem or API that throws on cancellation
            is IOException, is SocketException -> return
        // fine, some blocking code was interrupted by a de ispose call
            is InterruptedException -> return
        // that's likely a bug in the application
            is NullPointerException, is IllegalArgumentException -> {
                Thread.currentThread()
                    .uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return
            }
        // that's a bug in RxJava or in a custom operator
            is IllegalStateException -> {
                Thread.currentThread()
                    .uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return
            }
            else -> e
        }
        Log.w(this::class.java.simpleName, "Undeliverable throwable received, not sure what to do", exception)
    }
}