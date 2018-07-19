package com.popalay.cardme

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), NavHost {

    private var navHostFragment: View by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        navHostFragment = findViewById<View>(R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp() = navHostFragment.findNavController().navigateUp()

    override fun getNavController(): NavController = navHostFragment.findNavController()
}