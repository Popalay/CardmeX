package com.popalay.cardme.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import com.popalay.cardme.R
import com.popalay.cardme.core.util.UiModeDelegate
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), NavHost {

    private val uiModeDelegate: UiModeDelegate by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Cardme)
        uiModeDelegate.applyDayNight()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun getNavController(): NavController = findNavController(R.id.nav_host_fragment)
}