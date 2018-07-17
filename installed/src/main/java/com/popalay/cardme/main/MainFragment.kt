package com.popalay.cardme.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import com.popalay.cardme.R
import com.popalay.cardme.base.extensions.bindView

class MainFragment : Fragment() {

    companion object {

        fun newInstance() = MainFragment()
    }

    private val message: TextView by bindView(R.id.message)
    private val buttonLogIn: Button by bindView(R.id.button_log_in)

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLogIn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigateToLogInFragment))
    }
}