package com.popalay.cardme.remote

import com.google.firebase.firestore.FirebaseFirestore

internal val FirebaseFirestore.cards get() = collection("cards")

internal val FirebaseFirestore.users get() = collection("users")

internal val FirebaseFirestore.requests get() = collection("requests")