package com.popalay.cardme.api.data.persister

import com.popalay.cardme.api.data.Persister
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.model.Card

interface CardCachePersister : Persister<Card, Source.Cache> {

    sealed class Key : com.popalay.cardme.api.data.Key {

        data class ById(val cardId: String) : Key()
    }
}