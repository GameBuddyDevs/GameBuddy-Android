package com.example.gamebuddy.domain.usecase.auth

import android.accounts.Account
import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.account.AccountEntity
import com.example.gamebuddy.data.remote.model.verify.VerifyResponse
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.ForgotPasswordRequest
import com.example.gamebuddy.data.remote.request.RegisterRequest
import com.example.gamebuddy.data.remote.request.VerifyRequest
import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.util.Constants
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class ForgotPasswordUseCase(
    private val service: GameBuddyApiAuthService,
    private val accountDao: AccountDao,
    private val appDataStore: AppDataStore,
) {
    fun execute(
        email: String,
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())
        val account = accountDao.searchByEmail(email) ?: throw Exception("Email address not found")

        val forgotPasswordResponse = service.forgotPassword(
            forgotPasswordRequest = ForgotPasswordRequest(
                email = email
            )
        )
        if (!forgotPasswordResponse.status.success) {
            throw Exception(forgotPasswordResponse.status.message)
        }

        appDataStore.setValue(Constants.LAST_AUTH_USER, email)
        emit(DataState.success(response = null, data = true))
    }.catch { e ->
        Timber.e("ForgotPassword error: $e")
        emit(handleUseCaseException(e))
    }
}