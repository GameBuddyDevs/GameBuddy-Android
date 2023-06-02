package com.example.gamebuddy.domain.usecase.community

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.data.remote.request.JoinCommunityRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class LeaveCommunityUseCase(
    private val service: GameBuddyApiCommunityService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(
        communityId: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()?.token ?: ""

        if (authToken.isEmpty()) {
            Timber.d("Auth token is empty")
            throw Exception("Please login again.")
        }

        val response = service.leaveCommunity(
            token = "Bearer $authToken",
            community = JoinCommunityRequest(communityId = communityId)
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        emit(DataState.success(response = null, data = false))
    }.catch {
        emit(handleUseCaseException(it))
    }
}