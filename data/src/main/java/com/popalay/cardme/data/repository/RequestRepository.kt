package com.popalay.cardme.data.repository

import com.popalay.cardme.api.core.model.Request
import com.popalay.cardme.api.data.repository.RequestRepository
import com.popalay.cardme.api.remote.dao.RemoteRequestDao
import io.reactivex.Completable

class RequestRepository(
    private val remoteRequestDao: RemoteRequestDao
) : RequestRepository {

    override fun save(data: Request): Completable = remoteRequestDao.save(data).onErrorComplete()
}