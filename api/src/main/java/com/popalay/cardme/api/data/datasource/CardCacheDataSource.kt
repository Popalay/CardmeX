package com.popalay.cardme.api.data.datasource

import com.popalay.cardme.api.data.DataSource
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.model.Card

interface CardCacheDataSource : DataSource<Card, Source.Cache, CardCacheDataSource.Key> {

    sealed class Key : com.popalay.cardme.api.data.Key {

        object List : Key()
        data class ById(val cardId: String) : Key()
    }
}