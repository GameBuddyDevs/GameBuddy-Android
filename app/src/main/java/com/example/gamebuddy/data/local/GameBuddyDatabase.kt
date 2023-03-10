package com.example.gamebuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.account.AccountEntity
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.AuthTokenEntity

@Database(
    entities = [AccountEntity::class, AuthTokenEntity::class],
    version = 1
)
abstract class GameBuddyDatabase: RoomDatabase() {

    abstract fun getAccountInfoDao(): AccountDao

    abstract fun getAuthTokenDao(): AuthTokenDao
}