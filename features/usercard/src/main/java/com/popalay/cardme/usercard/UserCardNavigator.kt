package com.popalay.cardme.usercard

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.popalay.cardme.api.ui.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class UserCardNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as UserCardDestination) {
            UserCardDestination.AddCard -> {
                val bundle = bundleOf("isUserCard" to true.toString())
                navController.navigate(R.id.feature_add_card, bundle)
            }
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as UserCardDestination) {
            UserCardDestination.AddCard -> navController.popBackStack(R.id.feature_add_card, inclusive)
        }
    }
}

sealed class UserCardDestination : Destination {
    object AddCard : UserCardDestination()
}