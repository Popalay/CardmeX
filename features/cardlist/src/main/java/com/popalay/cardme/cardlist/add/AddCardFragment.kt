package com.popalay.cardme.cardlist.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.addcard.R
import com.popalay.cardme.core.widget.RoundedBottomSheetDialogFragment

class AddCardFragment : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_card_fragment, container, false)
}