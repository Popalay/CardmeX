package com.popalay.cardme.api.data.datasource

import com.popalay.cardme.api.data.DataSource
import com.popalay.cardme.api.data.Source

abstract class NetworkDataSource<T>(
    override val source: Source.Network = Source.Network
) : DataSource<T, Source.Network>