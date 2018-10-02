package com.popalay.cardme.addcard.model

import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.core.adapter.Identifiable

data class UserListItem(
    val user: User
) : Identifiable {
    override val id get() = user.uuid.hashCode().toLong()
}