package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.LoginRequest
import com.example.gamebuddy.domain.model.account.Account
import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.util.Constants
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlinx.coroutines.flow.Flow

class LoginUseCase (
    private val service: GameBuddyApiAuthService,
    private val accountDao: AccountDao,
    private val authTokenDao: AuthTokenDao,
    private val appDataStore: AppDataStore,
) {
    fun execute(
        email: String,
        password: String
    ): Flow<DataState<AuthToken>> = flow {
        emit(DataState.loading())
        val loginResponse = service.login(
            loginRequest = LoginRequest(
                email = email,
                password = password,
            )
        )
        if(!loginResponse.status.success){
            throw Exception(loginResponse.status.message)
        }

        accountDao.insertOrIgnore(
            accountEntity = Account(
                pk = loginResponse.authBody.authData.userId,
                email = email,
            ).toEntity()
        )
        val authToken = AuthToken(
            pk = loginResponse.authBody.authData.userId,
            token = loginResponse.authBody.authData.token
        )

        val result = authTokenDao.insertAuthToken(authToken.toEntity())
        if(result < 0){
            throw Exception("Error inserting auth token")
        }

        appDataStore.setValue(Constants.LAST_AUTH_USER, email)
        emit(DataState.success(response = null, data = authToken))
    }.catch { e->
        Timber.e("Login Use Case error: $e")
        emit(handleUseCaseException(e))
    }
}