package com.example.gamebuddy.di

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.network.GameBuddyApiMatchService
import com.example.gamebuddy.domain.usecase.main.MatchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MatchModule {

    @Singleton
    @Provides
    fun provideMatchUseCase(
        service: GameBuddyApiMatchService,
        authTokenDao: AuthTokenDao,
    ): MatchUseCase {
        return MatchUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

}