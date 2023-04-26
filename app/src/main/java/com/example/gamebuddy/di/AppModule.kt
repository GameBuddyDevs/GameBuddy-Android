package com.example.gamebuddy.di

import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.domain.usecase.auth.GamesUseCase
import com.example.gamebuddy.domain.usecase.auth.KeywordsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGamesUseCase(
        service: GameBuddyApiAppService,
    ): GamesUseCase {
        return GamesUseCase(
            service = service
        )
    }

    @Singleton
    @Provides
    fun provideKeywordsUseCase(
        service: GameBuddyApiAppService,
    ): KeywordsUseCase {
        return KeywordsUseCase(
            service = service
        )
    }
}