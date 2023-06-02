package com.example.gamebuddy.session

import com.example.gamebuddy.domain.model.account.AuthToken

sealed class SessionEvents {

    data class Login(val authToken: AuthToken): SessionEvents()

    object Logout: SessionEvents()

    data class ValidateToken(val authToken: AuthToken): SessionEvents()

    data class CheckPreviousAuthUser(val email: String): SessionEvents()

    object OnRemoveHeadFromQueue: SessionEvents()
}