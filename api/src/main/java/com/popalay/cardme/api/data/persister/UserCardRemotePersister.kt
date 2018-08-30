package com.popalay.cardme.api.data.persister

import com.popalay.cardme.api.data.Persister
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.model.Card

interface UserCardRemotePersister : Persister<Card, Source.Network>