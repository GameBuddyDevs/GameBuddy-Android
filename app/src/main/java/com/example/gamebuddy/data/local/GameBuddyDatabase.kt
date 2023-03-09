package com.example.gamebuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.account.AccountEntity

@Database(
    entities = [AccountEntity::class],
    version = 1
)
abstract class GameBuddyDatabase: RoomDatabase() {

    abstract fun getAccountInfoDao(): AccountDao
}