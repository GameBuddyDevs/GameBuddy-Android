package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.Pending.PendingFriends
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class PendingFriendUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
) {
    fun execute(): Flow<DataState<List<PendingFriends>>> = flow{
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenss: ${authToken?.token}")
        val friend = service.getPendingFriends(
            token = "Bearer ${authToken?.token}",
        ).toPendingFriends()
        if(friend.isNotEmpty()){
            Timber.d("PendingRequest Use Case success: ${friend[0].username}")
        }

        emit(DataState.success(response = null, data = friend))
    }.catch {
        Timber.e("PendingFriend Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}