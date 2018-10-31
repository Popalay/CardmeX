package com.popalay.cardme.addcard.model

import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.core.adapter.Identifiable

data class UserListItem(
    val user: User,
    val isProgress: Boolean
) : Identifiable {

    override val id: String = user.uuid
}