package com.popalay.cardme.api.data

sealed class Source {

    object Cache : Source()
    object Network : Source()
}