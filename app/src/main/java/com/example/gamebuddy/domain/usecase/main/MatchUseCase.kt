package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.user.User
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MatchUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
) {
    fun execute(): Flow<DataState<List<User>>> = flow {
        Timber.d("Match Use Case")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenns: ${authToken?.token}")
        val users = service.getUsers(
            token = "Bearer ${authToken?.token}"
        ).toUsers()

        Timber.d("User Use Case success: ${users?.get(0)?.gamerUsername}")
        emit(DataState.success(response = null, data = users))
    }.catch {
        Timber.e("User Use Case hataa $it")
        emit(handleUseCaseException(it))
    }
}