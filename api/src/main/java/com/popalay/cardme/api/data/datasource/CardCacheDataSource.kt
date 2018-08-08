package com.popalay.cardme.api.data.datasource

import com.popalay.cardme.api.data.DataSource
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.model.Card

interface CardCacheDataSource : DataSource<List<Card>, Source.Cache>