package com.popalay.cardme.cache.persister

import com.popalay.cardme.api.data.Persister
import com.popalay.cardme.api.data.Source

abstract class CachePersister<T>(
    override val source: Source.Cache = Source.Cache
) : Persister<T, Source.Cache>