package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.VerifyRequest
import com.example.gamebuddy.domain.model.account.Account
import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.util.Constants
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class VerifyUseCase(
    private val service: GameBuddyApiAuthService,
    private val authTokenDao: AuthTokenDao,
    private val appDataStore: AppDataStore
) {

    fun execute(
        pk: String,
        verificationCode: String
    ): Flow<DataState<AuthToken>> = flow {
        emit(DataState.loading())

        val email = appDataStore.getValue(Constants.LAST_AUTH_USER) ?: throw Exception("No last auth user found")

        val verifyResponse = service.verify(
            VerifyRequest(
                email = email,
                verificationCode = verificationCode
            )
        )

        if (!verifyResponse.status.success) {
            throw Exception(verifyResponse.status.message)
        }

        val authToken = AuthToken(
            pk = pk,
            token = verifyResponse.verifyBody.verifyData.accessToken
        )

        val result = authTokenDao.insertAuthToken(authToken.toEntity())

        if (result < 0) {
            throw Exception("Error inserting auth token")
        }

        appDataStore.setValue(Constants.IS_VERIFIED, result.toString()) // For auto login
        emit(DataState.success(response = null, data = authToken))
    }.catch { e ->
        Timber.d("UseCaseException: ${e.message}")
        emit(handleUseCaseException(e))
    }
}