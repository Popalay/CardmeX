package com.popalay.cardme.usercard

import androidx.fragment.app.Fragment
import com.popalay.cardme.addcard.AddCardFragmentArgs
import com.popalay.cardme.api.ui.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class UserCardNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as UserCardDestination) {
            UserCardDestination.AddCard -> {
                val bundle = AddCardFragmentArgs.Builder()
                    .setIsUserCard(true)
                    .build()
                    .toBundle()
                navController.navigate(R.id.action_from_user_card_to_add_card, bundle)
            }
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as UserCardDestination) {
            UserCardDestination.AddCard -> navController.popBackStack(R.id.action_from_user_card_to_add_card, inclusive)
        }
    }
}

sealed class UserCardDestination : Destination {
    object AddCard : UserCardDestination()
}