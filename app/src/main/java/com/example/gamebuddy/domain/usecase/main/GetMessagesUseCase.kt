package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.model.message.MessageData
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.request.SendFriendRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetMessagesUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
) {

    fun execute(matchedUserId: String): Flow<DataState<MessageData>> = flow {
        emit(DataState.loading())

        Timber.d("GetMessagesUseCase: $matchedUserId")

        val authToken = authTokenDao.getAuthToken()?.toAuthToken()

        val response = service.getMessages(
            token = "Bearer ${authToken?.token}",
            userId = matchedUserId
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        Timber.d("GetMessagesUseCase: ${response.body.data}")

        emit(DataState.success(response = null, data = response.body.data))
    }.catch { e ->
        Timber.e("GetMessagesUseCase Error: ${e.message}")
        emit(handleUseCaseException(e))
    }
}