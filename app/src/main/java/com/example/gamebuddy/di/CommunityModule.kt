package com.example.gamebuddy.di

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.domain.usecase.comments.GetCommentsUseCase
import com.example.gamebuddy.domain.usecase.comments.LikeCommentUseCase
import com.example.gamebuddy.domain.usecase.community.GetPostUseCase
import com.example.gamebuddy.domain.usecase.community.LikePostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommunityModule {

    @Singleton
    @Provides
    fun provideGetPostsUseCase(
        service: GameBuddyApiCommunityService
    ): GetPostUseCase {
        return GetPostUseCase(
            service = service
        )
    }

    @Singleton
    @Provides
    fun provideLikePostUseCase(
        service: GameBuddyApiCommunityService,
        authTokenDao: AuthTokenDao
    ): LikePostUseCase {
        return LikePostUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideGetCommentsUseCase(
        service: GameBuddyApiCommunityService
    ): GetCommentsUseCase {
        return GetCommentsUseCase(
            service = service
        )
    }

    @Singleton
    @Provides
    fun provideLikeCommentUseCase(
        service: GameBuddyApiCommunityService,
        authTokenDao: AuthTokenDao
    ): LikeCommentUseCase {
        return LikeCommentUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }


}