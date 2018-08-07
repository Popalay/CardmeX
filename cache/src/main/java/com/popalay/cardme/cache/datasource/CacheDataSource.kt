package com.popalay.cardme.cache.datasource

import com.popalay.cardme.api.data.DataSource
import com.popalay.cardme.api.data.Source

abstract class CacheDataSource<T>(
    override val source: Source.Cache = Source.Cache
) : DataSource<T, Source.Cache>