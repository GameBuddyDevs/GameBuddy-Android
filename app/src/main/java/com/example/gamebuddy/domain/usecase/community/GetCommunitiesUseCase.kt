package com.example.gamebuddy.domain.usecase.community

import com.example.gamebuddy.data.remote.model.joincommunity.Community
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetCommunitiesUseCase(
    private val service: GameBuddyApiCommunityService
) {

    fun execute(): Flow<DataState<List<Community>>> = flow {
        emit(DataState.loading())

        val response = service.getCommunities()

        if (!response.status.success)
            throw Exception(response.status.message)

        val communities = response.body.data.communities

        emit(DataState.success(response = null, data = communities))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}