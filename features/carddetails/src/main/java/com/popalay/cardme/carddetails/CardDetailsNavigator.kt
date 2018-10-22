package com.popalay.cardme.carddetails

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.ui.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class CardDetailsNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
    }
}

sealed class CardDetailsDestination : Destination {
}