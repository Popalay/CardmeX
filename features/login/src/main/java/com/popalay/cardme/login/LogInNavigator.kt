package com.popalay.cardme.login

import androidx.fragment.app.Fragment
import com.popalay.cardme.api.navigation.Destination
import com.popalay.cardme.core.navigation.BaseNavigator

class LogInNavigator(fragment: Fragment) : BaseNavigator(fragment) {

    override fun navigate(destination: Destination) {
        throw UnsupportedOperationException("There is no forward navigation form Log in screen")
    }

    override fun popBackStackTo(destination: Destination, inclusive: Boolean) {
        throw UnsupportedOperationException("There is no back to navigation form Log in screen")
    }
}

sealed class LogInDestination : Destination {
}