package com.popalay.cardme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import com.popalay.shaketoreport.Config
import com.popalay.shaketoreport.ReportTimeout
import com.popalay.shaketoreport.ShakeToReport
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), NavHost {

    private var shakeToReport: ShakeToReport by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Cardme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val config = Config(BuildConfig.ENABLE_BUG_REPOTRING, ReportTimeout(5, TimeUnit.MINUTES))
        shakeToReport = ShakeToReport(this, config)
        lifecycle.addObserver(shakeToReport)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(shakeToReport)
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()

    override fun getNavController(): NavController = Navigation.findNavController(this, R.id.nav_host_fragment)
}