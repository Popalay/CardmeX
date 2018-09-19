package com.popalay.cardme.api.data.repository

import io.reactivex.Completable

interface DeviceRepository {

    fun saveToClipboard(label: String, text: String): Completable
}