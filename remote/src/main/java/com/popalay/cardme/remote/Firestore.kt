package com.popalay.cardme.remote

import com.google.firebase.firestore.FirebaseFirestore

internal val FirebaseFirestore.cards get() = FirebaseFirestore.getInstance().collection("cards")

internal val FirebaseFirestore.holders get() = FirebaseFirestore.getInstance().collection("holders")