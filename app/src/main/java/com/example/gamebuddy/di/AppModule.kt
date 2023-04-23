package com.example.gamebuddy.di

import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.domain.usecase.auth.GamesUseCase
import com.example.gamebuddy.domain.usecase.auth.KeywordsUseCase
import dagger.Provides
import javax.inject.Singleton

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
        service: GameBuddyApiAuthService,
    ): KeywordsUseCase {
        return KeywordsUseCase(
            service = service
        )
    }
}