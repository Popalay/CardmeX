package com.popalay.cardme.carddetails

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.ui.state.ViewState

internal data class CardDetailsViewState(
    val card: Card? = null,
    val progress: Boolean = false,
    val saveProgress: Boolean = false,
    val error: Throwable? = null
) : ViewState