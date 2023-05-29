package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.avatar.Avatar
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class AvatarUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao

) {
    fun execute(): Flow<DataState<List<Avatar>>> = flow {
        Timber.d("Avatar Use Case: ")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenns: ${authToken?.token}")
        val avatars = service.getAvatar(
            token = "Bearer ${authToken?.token}"
        ).toAvatar()

        Timber.d("Avatar Use Case success: ${avatars?.get(0)?.id}")

        emit(DataState.success(response = null, data = avatars))
    }.catch {
        Timber.e("Avatar Use Case hatakeeee: $it")
        emit(handleUseCaseException(it))
    }
}