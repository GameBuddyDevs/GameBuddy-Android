package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.model.basic.BasicResponse
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.request.SendFriendRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SendFriendRequestUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
) {

    fun execute(matchedUserId: String): Flow<DataState<String>> = flow {
        emit(DataState.loading())

        Timber.d("SendFriendRequestUseCase: $matchedUserId")

        val authToken = authTokenDao.getAuthToken()?.toAuthToken()

        val response = service.sendFriendRequest(
            token = "Bearer ${authToken?.token}",
            userId = SendFriendRequest(userId = matchedUserId)
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        emit(DataState.success(response = null, data = response.body.data.message))
    }.catch {
        emit(handleUseCaseException(it))
    }
}