package com.example.gamebuddy.domain.model.account

import com.example.gamebuddy.data.local.auth.AuthTokenEntity

data class AuthToken(
    val pk: String,
    val token: String?
) {
    fun toEntity(): AuthTokenEntity {
        return AuthTokenEntity(
            account_pk = pk,
            token = token
        )
    }
}