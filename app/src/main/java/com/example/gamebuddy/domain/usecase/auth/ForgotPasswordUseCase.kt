package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ForgotPasswordUseCase(
    private val service: GameBuddyApiAuthService,
    private val accountDao: AccountDao,
    private val appDataStore: AppDataStore,
) {
    fun execute(
        email:String,
    ): Flow<DataState<Boolean>> = flow{
        emit(DataState.loading())
        //...
    }
}