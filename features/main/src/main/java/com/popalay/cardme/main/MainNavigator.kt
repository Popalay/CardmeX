package com.popalay.cardme.main

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.ui.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class MainNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.navigate(R.id.action_to_user_card)
            MainDestination.AddCard -> navController.navigate(R.id.action_to_add_card)
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.popBackStack(R.id.action_to_user_card, inclusive)
            MainDestination.AddCard -> navController.popBackStack(R.id.action_to_add_card, inclusive)
        }
    }
}

sealed class MainDestination : Destination {
    object UserCard : MainDestination()
    object AddCard : MainDestination()
}