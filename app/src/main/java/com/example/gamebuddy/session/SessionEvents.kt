package com.example.gamebuddy.session

sealed class SessionEvents {

    object Logout: SessionEvents()

    data class Login(val email: String, val password: String): SessionEvents()

    data class Register(val email: String, val password: String): SessionEvents()

    data class CheckPreviousAuthUser(val email: String): SessionEvents()

    object OnRemoveHeadFromQueue: SessionEvents()
}