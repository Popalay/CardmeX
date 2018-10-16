package com.popalay.cardme.api.remote.dao

import com.popalay.cardme.api.core.model.Request
import io.reactivex.Completable

interface RemoteRequestDao {

    fun save(data: Request): Completable
}