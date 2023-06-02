package com.example.gamebuddy.domain.usecase.community

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.post.Post
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetPostFromCommunityUseCase(
    private val service: GameBuddyApiCommunityService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(
        communityId: String
    ): Flow<DataState<List<Post>>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()

        Timber.d("coMMUNITY ID: $communityId")

        val response = service.getPosts(
            token = "Bearer ${authToken?.token}",
            communityId = communityId
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        val posts = response.body.data.posts

        emit(DataState.success(response = null, data = posts))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}