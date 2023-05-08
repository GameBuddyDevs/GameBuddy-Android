package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.model.basic.BasicResponse
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.request.AcceptRejectFriendRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class acceptFriendsUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
) {
    fun execute(
        userId: String?,
        accept: Boolean?
    ): Flow<DataState<Boolean>> = flow{
        emit(DataState.loading())

        val request = AcceptRejectFriendRequest(userId!!)
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        val response: BasicResponse = if(accept == true){
            service.acceptFriends(
                token = "Bearer ${authToken?.token}",
                acceptRejectFriendRequest = request
            )
        } else{
            service.rejectFriends(
                token = "Bearer ${authToken?.token}",
                acceptRejectFriendRequest = request
            )
        }

        if (!response.status.success) {
            Timber.e("AcceptFriendsUseCase ${response.status.message}")
            throw Exception(response.status.message)
        }
        emit(DataState.success(response = null, data = true))
    }.catch { e ->
        Timber.e("AcceptFriendsUseCase ERROR: $e")
        emit(handleUseCaseException(e))
    }
}