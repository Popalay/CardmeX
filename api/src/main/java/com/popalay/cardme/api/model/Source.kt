package com.popalay.cardme.api.model

sealed class Source {

    object Cache : Source()
    object Network : Source()
}