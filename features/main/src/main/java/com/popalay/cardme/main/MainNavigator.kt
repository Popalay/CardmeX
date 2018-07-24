package com.popalay.cardme.main

import android.support.v4.app.Fragment
import com.popalay.cardme.api.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class MainNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        when (destination as MainDestination) {
            MainDestination.LogIn -> navController.navigate(R.id.action_from_main_to_log_in)
        }
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        when (destination as MainDestination) {
            MainDestination.LogIn -> navController.popBackStack(R.id.action_from_main_to_log_in, inclusive)
        }
    }
}

sealed class MainDestination : Destination {
    object LogIn : MainDestination()
}