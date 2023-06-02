package com.example.gamebuddy.domain.usecase.community

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.post.Post
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetPostsUseCase(
    private val service: GameBuddyApiCommunityService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(): Flow<DataState<List<Post>>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()

        val response = service.getJoinedPosts(
            token = "Bearer ${authToken?.token}",
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        val posts = response.body.data.posts

        emit(DataState.success(response = null, data = posts))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}