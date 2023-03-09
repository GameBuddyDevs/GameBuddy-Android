package com.example.gamebuddy.di

import android.app.Application
import androidx.room.Room
import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.datastore.AppDataStoreManager
import com.example.gamebuddy.data.local.GameBuddyDatabase
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideDataStoreManager(
        application: Application
    ): AppDataStore {
        return AppDataStoreManager(application)
    }

    @Singleton
    @Provides
    fun provideGameBuddyDatabase(
        application: Application
    ): GameBuddyDatabase {
        return Room
            .databaseBuilder(context = application, klass = GameBuddyDatabase::class.java, name = Constants.ROOM_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAccountDao(
        gameBuddyDatabase: GameBuddyDatabase
    ): AccountDao {
        return gameBuddyDatabase.getAccountInfoDao()
    }

    @Singleton
    @Provides
    fun provideAuthTokenDao(
        gameBuddyDatabase: GameBuddyDatabase
    ): AuthTokenDao {
        return gameBuddyDatabase.getAuthTokenDao()
    }

}