package com.popalay.cardme.main

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class MainNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.navigate(R.id.action_from_main_to_user_card)
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.popBackStack(R.id.action_from_main_to_user_card, inclusive)
        }
    }
}

sealed class MainDestination : Destination {
    object UserCard : MainDestination()
}