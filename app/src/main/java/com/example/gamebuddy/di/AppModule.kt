package com.example.gamebuddy.di

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.usecase.auth.GamesUseCase
import com.example.gamebuddy.domain.usecase.auth.KeywordsUseCase
import com.example.gamebuddy.domain.usecase.main.GetChatBoxUseCase
import com.example.gamebuddy.domain.usecase.main.GetFriendsUseCase
import com.example.gamebuddy.domain.usecase.main.MatchUseCase
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

    @Singleton
    @Provides
    fun provideGetFriendsUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): GetFriendsUseCase {
        return GetFriendsUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideGetChatBoxUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): GetChatBoxUseCase{
        return GetChatBoxUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

}