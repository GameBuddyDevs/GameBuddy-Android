package com.example.gamebuddy.di

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.datastore.AppDataStoreManager
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.domain.usecase.auth.RegisterUseCase
import com.example.gamebuddy.domain.usecase.auth.VerifyUseCase
import com.example.gamebuddy.domain.usecase.session.CheckPreviousAuthUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideCheckPreviousAuthUser(
        accountDao: AccountDao,
        authTokenDao: AuthTokenDao
    ): CheckPreviousAuthUserUseCase {
        return CheckPreviousAuthUserUseCase(
            accountDao = accountDao,
            authTokenDao = authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideRegisterUseCase(
        service: GameBuddyApiAuthService,
        accountDao: AccountDao,
        authTokenDao: AuthTokenDao,
        dataStore: AppDataStore
    ): RegisterUseCase {
        return RegisterUseCase(
            service = service,
            accountDao = accountDao,
            //authTokenDao = authTokenDao,
            appDataStore = dataStore
        )
    }

    @Singleton
    @Provides
    fun provideVerifyUseCase(
        service: GameBuddyApiAuthService,
        authTokenDao: AuthTokenDao,
        dataStore: AppDataStore
    ): VerifyUseCase {
        return VerifyUseCase(
            service = service,
            authTokenDao = authTokenDao,
            appDataStore = dataStore
        )
    }
}