package com.popalay.cardme.cardlist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.popalay.cardme.addcard.AddCardFragment
import com.popalay.cardme.addcard.R
import com.popalay.cardme.core.extensions.bindView

class CardListFragment : Fragment() {

    private val buttonAddCard: Button by bindView(R.id.button_add_card)

    private lateinit var viewModel: CardListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.card_list_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CardListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonAddCard.setOnClickListener {
            AddCardFragment().show(fragmentManager, null)
        }
    }
}