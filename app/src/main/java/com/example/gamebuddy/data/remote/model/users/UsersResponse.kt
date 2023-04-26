package com.example.gamebuddy.data.remote.model.users

import com.example.gamebuddy.data.remote.model.register.AuthStatus
import com.example.gamebuddy.domain.model.user.User

data class UsersResponse(
    val body: UsersBody,
    val status: AuthStatus
    ) {

    fun toUsers(): List<User>{
        return body.data.userDto.map { it.toUser() }
    }
}