package com.popalay.cardme.api.repository

import io.reactivex.Completable

interface DeviceRepository {

    fun saveToClipboard(label: String, text: String): Completable
}