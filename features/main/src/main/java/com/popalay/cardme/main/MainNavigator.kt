package com.popalay.cardme.main

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.popalay.cardme.api.ui.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class MainNavigator(val fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        val navController = Navigation.findNavController(fragment.requireActivity(), R.id.nav_host_fragment)
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.navigate(R.id.feature_user_card)
            MainDestination.AddCard -> navController.navigate(R.id.feature_add_card)
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as MainDestination) {
            MainDestination.UserCard -> navController.popBackStack(R.id.feature_user_card, inclusive)
            MainDestination.AddCard -> navController.popBackStack(R.id.feature_add_card, inclusive)
        }
    }
}

sealed class MainDestination : Destination {
    object UserCard : MainDestination()
    object AddCard : MainDestination()
}