package com.popalay.cardme.api.data.datasource

import com.popalay.cardme.api.data.DataSource
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.model.User

interface UserRemoteDataSource : DataSource<User?, Source.Network, UserRemoteDataSource.Key> {

    data class Key(val userId: String) : com.popalay.cardme.api.data.Key
}