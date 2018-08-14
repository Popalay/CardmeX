package com.popalay.cardme.core.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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

    protected val navController = Navigation.findNavController(requireNotNull(fragment.view))

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}