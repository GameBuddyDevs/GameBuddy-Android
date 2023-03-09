package com.example.gamebuddy.data.local.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthToken(authTokenEntity: AuthTokenEntity): Long

    @Query("DELETE FROM auth_token")
    suspend fun deleteAuthTokens()

    @Query("SELECT * FROM auth_token WHERE account_pk = :pk")
    suspend fun searchByPk(pk: String): AuthTokenEntity?

}