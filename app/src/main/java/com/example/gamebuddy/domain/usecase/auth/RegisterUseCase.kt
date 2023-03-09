package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStoreManager
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService

class RegisterUseCase(
    private val service: GameBuddyApiAuthService,
    private val accountDao: AccountDao,
    private val appDataStoreManager: AppDataStoreManager
) {

}