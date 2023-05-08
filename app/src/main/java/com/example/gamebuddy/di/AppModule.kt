package com.example.gamebuddy.di

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.usecase.auth.GamesUseCase
import com.example.gamebuddy.domain.usecase.auth.KeywordsUseCase
import com.example.gamebuddy.domain.usecase.main.GetChatBoxUseCase
import com.example.gamebuddy.domain.usecase.main.GetFriendsUseCase
import com.example.gamebuddy.domain.usecase.main.GetMessagesFromWebSocketUseCase
import com.example.gamebuddy.domain.usecase.main.GetMessagesUseCase
import com.example.gamebuddy.domain.usecase.main.PendingFriendUseCase
import com.example.gamebuddy.domain.usecase.main.ProfileUseCase
import com.example.gamebuddy.domain.usecase.main.SendFriendRequestUseCase
import com.example.gamebuddy.domain.usecase.main.SendMessageUseCase
import com.example.gamebuddy.domain.usecase.main.acceptFriendsUseCase
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
    fun providePendingFriendUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): PendingFriendUseCase {
        return PendingFriendUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }
    @Singleton
    @Provides
    fun provideacceptFriendsUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): acceptFriendsUseCase {
        return acceptFriendsUseCase(
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

    @Singleton
    @Provides
    fun provideProfileUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao,
    ): ProfileUseCase {
        return ProfileUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideSendFriendRequestUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): SendFriendRequestUseCase {
        return SendFriendRequestUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideGetMessagesUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): GetMessagesUseCase {
        return GetMessagesUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideSendMessageUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): SendMessageUseCase {
        return SendMessageUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideGetMessagesFromWebSocketUseCase(
        service: GameBuddyApiAppService,
        authTokenDao: AuthTokenDao
    ): GetMessagesFromWebSocketUseCase {
        return GetMessagesFromWebSocketUseCase(
            authTokenDao = authTokenDao
        )
    }


}