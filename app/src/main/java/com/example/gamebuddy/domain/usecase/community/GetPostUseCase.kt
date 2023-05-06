package com.example.gamebuddy.domain.usecase.community

import com.example.gamebuddy.data.remote.model.post.Post
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetPostUseCase(
    private val service: GameBuddyApiAppService
) {

    fun execute(
        communityId: String
    ): Flow<DataState<List<Post>>> = flow {
        emit(DataState.loading())

        val response = service.getPosts(communityId)

        if (!response.status.success)
            throw Exception(response.status.message)

        val posts = response.body.data.posts

        emit(DataState.success(response = null, data = posts))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}