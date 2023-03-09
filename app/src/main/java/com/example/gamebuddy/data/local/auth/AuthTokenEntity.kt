package com.example.gamebuddy.data.local.auth

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gamebuddy.data.local.account.AccountEntity
import com.example.gamebuddy.domain.model.account.AuthToken
import com.google.gson.annotations.Expose

@Entity(
    tableName = "auth_token",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["pk"],
            childColumns = ["account_pk"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AuthTokenEntity(

    @PrimaryKey
    @ColumnInfo(name = "account_pk")
    val account_pk: String = "-1",

    @ColumnInfo(name = "token")
    @Expose
    val token: String? = null,
)

fun AuthTokenEntity.toAuthToken(): AuthToken {
    return AuthToken(
        pk = account_pk,
        token = token
    )
}
