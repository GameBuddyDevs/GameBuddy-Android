package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.NewPasswordRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class NewPasswordUseCase(
    private val service: GameBuddyApiAuthService,
    private val authTokenDao: AuthTokenDao,
) {
    fun execute(
        password: String,
        confirmPassword:String,
    ):Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokensss: ${authToken?.token}")
        val newPasswordResponse = service.newPassword(
            token = "Bearer ${authToken?.token}",
            newPasswordRequest = NewPasswordRequest(password = password)
        )
        if (!newPasswordResponse.status.success) {
            throw Exception(newPasswordResponse.status.message)
        }
        emit(DataState.success(response = null, data = true))
    }.catch {  e ->
        Timber.e("New Password Error: $e")
        emit(handleUseCaseException(e)) }
}