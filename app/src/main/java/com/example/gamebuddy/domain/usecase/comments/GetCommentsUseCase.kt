package com.example.gamebuddy.domain.usecase.comments

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.comment.Comment
import com.example.gamebuddy.data.remote.model.comment.CommentData
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetCommentsUseCase(
    private val service: GameBuddyApiCommunityService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(postId: String): Flow<DataState<List<Comment>>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()?.token ?: ""

        if (authToken.isEmpty()){
            Timber.d("Auth token is empty")
            throw Exception("Please login again.")
        }

        val response = service.getComments(
            token = "Bearer $authToken",
            postId = postId
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        emit(DataState.success(response = null, data = response.body.data.comments))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}