package org.maurycy.models.requests

import org.maurycy.enums.UserStatus
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class UserRequest(
    val email: String,
    val userName: String,
    val password: String,

    @Enumerated(EnumType.STRING)
    val userStatus: UserStatus
):ModelRequestIF


