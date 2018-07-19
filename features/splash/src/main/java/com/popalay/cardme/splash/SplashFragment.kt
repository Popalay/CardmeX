package com.popalay.cardme.splash

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread.sleep(500L)
        if (FirebaseAuth.getInstance().currentUser == null) {
            view.findNavController().navigate(R.id.navigateToLogInFragment)
        } else {
            Toast.makeText(context, "You are logged id", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(R.id.navigateToMainFragment)
        }
    }
}