package com.popalay.cardme.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import com.popalay.cardme.R


class MainActivity : AppCompatActivity(), NavHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Cardme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun getNavController(): NavController = findNavController(R.id.nav_host_fragment)

    fun restart() {
        overridePendingTransition(0, 0)
        recreate()
    }
}