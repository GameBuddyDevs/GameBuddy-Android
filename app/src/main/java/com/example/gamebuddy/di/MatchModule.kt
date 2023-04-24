package com.example.gamebuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MatchModule {

//    @Singleton
//    @Provides
//    fun provideMatchService(
//        service: GameBuddyApiMatchService
//    ): MatchService {
//        return MatchService(
//            service = service
//        )
//    }
}