package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.profile.AllFriends
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetAllFriendsUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
)  {
    fun execute(): Flow<DataState<List<AllFriends>>> = flow {
        Timber.d("Get All Friends Use Case")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenss: ${authToken?.token}")
        val friends = service.getAllFriends(
            token = "Bearer ${authToken?.token}"
        ).toFriends()
        if (friends.isNotEmpty())
        {
            Timber.d("Get All Friends Use Case success: ${friends[0].username}")
        }
        emit(DataState.success(response = null, data = friends))
    }.catch {
        Timber.e("Profile Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}