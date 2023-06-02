package com.example.gamebuddy.domain.usecase.session

import com.example.gamebuddy.data.local.account.AccountDao
import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.MessageType
import com.example.gamebuddy.util.Response
import com.example.gamebuddy.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CheckPreviousAuthUserUseCase(
    private val accountDao: AccountDao,
    private val authTokenDao: AuthTokenDao,
) {
    fun execute(
        email: String,
    ): Flow<DataState<AuthToken>> = flow {
        emit(DataState.loading())

        var authToken: AuthToken? = null
        val account = accountDao.searchByEmail(email)

        if (account != null) {
            authToken = authTokenDao.searchByPk(account.pk)?.toAuthToken()
            if (authToken != null) {
                emit(DataState.success(response = null, data = authToken))
            } else {
                throw Exception("Error retrieving auth token. No previous user found")
            }
        } else {
            throw Exception("Error retrieving account properties. No previous user found")
        }
    }.catch { exception ->
        Timber.e("ALOOOO exceptionske $exception")
        exception.printStackTrace()
        emit(
            DataState.error<AuthToken>(
                response = Response(
                    "Done checking for previously authenticated user.",
                    UIComponentType.None(),
                    MessageType.Error()
                )
            )
        )
    }
}
