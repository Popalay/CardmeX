package com.popalay.cardme.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.popalay.cardme.base.extensions.bindView
import com.popalay.cardme.base.widget.ProgressMaterialButton

class MainFragment : Fragment() {

    private val buttonUnsync: ProgressMaterialButton by bindView(R.id.button_unsync)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUnsync.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_from_main_to_log_in)
        }
    }
}