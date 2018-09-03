package com.popalay.cardme.api.data.persister

import com.popalay.cardme.api.data.Persister
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.model.Card

interface CardRemotePersister : Persister<Card, Source.Network> {

    sealed class Key : com.popalay.cardme.api.data.Key {

        data class ById(val cardId: String) : Key()
    }
}