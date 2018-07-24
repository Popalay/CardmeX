package com.popalay.cardme.api.navigation

interface Navigator {

    fun navigate(destination: Destination)

    fun popBackStack()

    fun navigateUp()

    fun popBackStackTo(destination: Destination, inclusive: Boolean = false)
}

interface Destination

interface NavigatorHolder {

    var navigator: Navigator?
}

interface Router : Navigator {

    val navigationHolder: NavigatorHolder
}