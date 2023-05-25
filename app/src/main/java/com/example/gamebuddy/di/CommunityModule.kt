package com.example.gamebuddy.di

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.domain.usecase.comments.CreateCommentUseCase
import com.example.gamebuddy.domain.usecase.comments.GetCommentsUseCase
import com.example.gamebuddy.domain.usecase.comments.LikeCommentUseCase
import com.example.gamebuddy.domain.usecase.community.GetCommunitiesUseCase
import com.example.gamebuddy.domain.usecase.community.GetPostFromCommunityUseCase
import com.example.gamebuddy.domain.usecase.community.GetPostsUseCase
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
    fun provideGetCommunitiesUseCase(
        service: GameBuddyApiCommunityService
    ): GetCommunitiesUseCase {
        return GetCommunitiesUseCase(
            service = service
        )
    }

    @Singleton
    @Provides
    fun provideGetPostFromCommunityUseCase(
        service: GameBuddyApiCommunityService,
        authTokenDao: AuthTokenDao
    ): GetPostsUseCase {
        return GetPostsUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideGetPostsUseCase(
        service: GameBuddyApiCommunityService,
        authTokenDao: AuthTokenDao
    ): GetPostFromCommunityUseCase {
        return GetPostFromCommunityUseCase(
            service = service,
            authTokenDao = authTokenDao
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
        service: GameBuddyApiCommunityService,
        authTokenDao: AuthTokenDao
    ): GetCommentsUseCase {
        return GetCommentsUseCase(
            service = service,
            authTokenDao = authTokenDao
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

    @Singleton
    @Provides
    fun provideCreateCommentUseCase(
        service: GameBuddyApiCommunityService,
        authTokenDao: AuthTokenDao
    ): CreateCommentUseCase {
        return CreateCommentUseCase(
            service = service,
            authTokenDao = authTokenDao
        )
    }

}