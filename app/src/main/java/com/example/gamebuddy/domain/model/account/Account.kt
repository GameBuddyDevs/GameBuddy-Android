package com.example.gamebuddy.domain.model.account

import com.example.gamebuddy.data.local.account.AccountEntity

data class Account(
    val pk: String,
    val email: String
) {
    fun toEntity(): AccountEntity {
        return AccountEntity(
            pk = pk,
            email = email
        )
    }
}
