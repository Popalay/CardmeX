package com.popalay.cardme.core.navigation

import android.support.v4.app.Fragment
import androidx.navigation.fragment.findNavController
import com.popalay.cardme.api.navigation.Destination
import com.popalay.cardme.api.navigation.Navigator
import com.popalay.cardme.api.navigation.NavigatorHolder
import com.popalay.cardme.api.navigation.Router

class BaseRouter(override val navigationHolder: NavigatorHolder) : Router, Navigator {

    override fun popBackStack() {
        navigationHolder.navigator?.popBackStack()
    }

    override fun navigateUp() {
        navigationHolder.navigator?.navigateUp()
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        navigationHolder.navigator?.popBackStackTo(destination, inclusive)
    }

    override fun navigate(destination: Destination) {
        navigationHolder.navigator?.navigate(destination)
    }
}

class BaseNavigationHolder : NavigatorHolder {

    override var navigator: Navigator? = null
}

abstract class BaseNavigator(fragment: Fragment) : Navigator {

    protected val navController = fragment.findNavController()

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}