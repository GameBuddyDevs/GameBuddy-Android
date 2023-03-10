package com.example.gamebuddy.session

import com.example.gamebuddy.domain.model.account.AuthToken

sealed class SessionEvents {

    object Logout: SessionEvents()

    data class Login(val authToken: AuthToken): SessionEvents()

    data class Register(val email: String, val password: String): SessionEvents()

    data class CheckPreviousAuthUser(val email: String): SessionEvents()

    object OnRemoveHeadFromQueue: SessionEvents()
}