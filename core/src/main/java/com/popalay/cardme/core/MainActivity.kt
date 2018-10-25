package com.popalay.cardme.core

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.popalay.cardme.R

class MainActivity : AppCompatActivity(), NavHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Cardme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { data ->
                val deepLink = data?.link ?: return@addOnSuccessListener
                val intent = data.getUpdateAppIntent(this) ?: Intent(Intent.ACTION_VIEW).apply {
                    this.data = deepLink
                }
                startActivity(intent)
            }
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()

    override fun getNavController(): NavController = Navigation.findNavController(this, R.id.nav_host_fragment)
}