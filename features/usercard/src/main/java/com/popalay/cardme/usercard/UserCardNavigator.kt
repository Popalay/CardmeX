package com.popalay.cardme.usercard

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.ui.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class UserCardNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as UserCardDestination) {
            UserCardDestination.AddCard -> navController.navigate(UserCardFragmentDirections.actionFromUserCardToAddCard())
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as UserCardDestination) {
            UserCardDestination.AddCard -> navController.popBackStack(UserCardFragmentDirections.actionFromUserCardToAddCard().actionId, inclusive)
        }
    }
}

sealed class UserCardDestination : Destination {
    object AddCard : UserCardDestination()
}