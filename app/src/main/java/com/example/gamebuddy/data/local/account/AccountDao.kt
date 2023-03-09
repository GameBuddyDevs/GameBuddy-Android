package com.example.gamebuddy.data.local.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(accountEntity: AccountEntity): Long

    @Query("UPDATE account_prop SET email = :email WHERE pk = :pk")
    suspend fun updateAccount(pk: Int, email: String)

    @Query("SELECT * FROM account_prop WHERE email = :email")
    suspend fun searchByEmail(email: String): AccountEntity?

    @Query("SELECT * FROM account_prop WHERE pk = :pk")
    suspend fun searchByPk(pk: Int): AccountEntity?
}