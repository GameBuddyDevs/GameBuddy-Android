package com.example.gamebuddy.di

import android.app.Application
import androidx.room.Room
import com.example.gamebuddy.data.local.GameBuddyDatabase
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
    fun provideGameBuddyDatabase(
        application: Application
    ): GameBuddyDatabase {
        return Room
            .databaseBuilder(context = application, klass = GameBuddyDatabase::class.java, name = Constants.ROOM_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}