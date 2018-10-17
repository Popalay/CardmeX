package com.popalay.cardme.main

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.ui.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class MainNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.navigate(MainFragmentDirections.actionFromMainToUserCard())
            MainDestination.AddCard -> navController.navigate(MainFragmentDirections.actionFromMainToAddCard())
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.popBackStack(MainFragmentDirections.actionFromMainToUserCard().actionId, inclusive)
            MainDestination.AddCard -> navController.popBackStack(MainFragmentDirections.actionFromMainToAddCard().actionId, inclusive)
        }
    }
}

sealed class MainDestination : Destination {
    object UserCard : MainDestination()
    object AddCard : MainDestination()
}