package com.popalay.cardme.core.mapper

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.firebase.auth.FirebaseUser
import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.DisplayName
import com.popalay.cardme.api.core.model.User

class FirebaseUserToUserMapper : Mapper<FirebaseUser?, Optional<User>> {

    override fun apply(value: FirebaseUser?): Optional<User> =
        if (value == null) None
        else User(
            uuid = value.uid,
            email = value.email ?: "",
            phoneNumber = value.phoneNumber ?: "",
            photoUrl = value.photoUrl?.toString() ?: "",
            displayName = DisplayName(value.displayName ?: "Undefined"),
            cardId = ""
        ).toOptional()
}