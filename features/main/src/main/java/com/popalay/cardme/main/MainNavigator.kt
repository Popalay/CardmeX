package com.popalay.cardme.main

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class MainNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as MainDestination) {
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as MainDestination) {
        }
    }
}

sealed class MainDestination : Destination {
}