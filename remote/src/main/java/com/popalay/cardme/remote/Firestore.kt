package com.popalay.cardme.remote

import com.google.firebase.firestore.FirebaseFirestore

internal val cardFirestore get() = FirebaseFirestore.getInstance().collection("cards")

internal val holderFirestore get() = FirebaseFirestore.getInstance().collection("holders")