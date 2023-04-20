package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.usernameRequest
import com.example.gamebuddy.util.Constants
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class UsernameUseCase(
    private val service: GameBuddyApiAuthService,
    private val accountDao: AccountDao,
    private val authTokenDao: AuthTokenDao,
    private val appDataStore: AppDataStore,
) {
    fun execute(
        username:String
    ):Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        val usernameResponse =service.username(
            token = "Bearer ${authToken?.token}",
            usernameRequest = usernameRequest(
                username = username
            )
        )
        if(!usernameResponse.status.success){
            throw Exception(usernameResponse.status.message)
        }
        emit(DataState.success(response = null, data = true))
    }.catch { e ->
        Timber.e("Username error: $e")
        emit(handleUseCaseException(e))
    }
}