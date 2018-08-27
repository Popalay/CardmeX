package com.popalay.cardme.remote

import com.google.firebase.firestore.FirebaseFirestore

internal val FirebaseFirestore.cards get() = FirebaseFirestore.getInstance().collection("cards")

internal val FirebaseFirestore.users get() = FirebaseFirestore.getInstance().collection("users")