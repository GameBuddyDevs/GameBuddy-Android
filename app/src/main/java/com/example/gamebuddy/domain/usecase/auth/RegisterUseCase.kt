package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.data.remote.request.RegisterRequest
import com.example.gamebuddy.domain.model.account.Account
import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.util.Constants
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RegisterUseCase(
    private val service: GameBuddyApiAuthService,
    private val accountDao: AccountDao,
    //private val authTokenDao: AuthTokenDao,
    private val appDataStore: AppDataStore,
) {

    fun execute(
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())
        val registerResponse = service.register(
            registerRequest = RegisterRequest(
                email = email,
                password = password,
            )
        )

        if (!registerResponse.status.success) {
            throw Exception(registerResponse.status.message)
        }

        accountDao.insertAccount(
            accountEntity = Account(
                pk = registerResponse.authBody.authData.userId,
                email = email,
            ).toEntity()
        )

//        val authToken = AuthToken(
//            pk = registerResponse.authBody.authData.userId,
//            token = registerResponse.authBody.authData.token,
//        )

//        val result = authTokenDao.insertAuthToken(authToken.toEntity())
//        if (result < 0) {
//            throw Exception("Error inserting auth token")
//        }

        appDataStore.setValue(Constants.LAST_AUTH_USER, email) // For auto login
        emit(DataState.success(response = null, data = true))
    }.catch { e ->
        Timber.e("RegisterUseCase hatakeeee: $e")
        emit(handleUseCaseException(e))
    }
}