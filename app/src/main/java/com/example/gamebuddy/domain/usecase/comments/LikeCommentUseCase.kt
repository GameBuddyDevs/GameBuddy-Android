package com.example.gamebuddy.domain.usecase.comments

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LikeCommentUseCase(
    private val service: GameBuddyApiCommunityService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(
        commentId: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()

        val response = service.likeComment(
            token = authToken?.token ?: "",
            commentId = commentId
        )

        if (!response.status.success)
            throw Exception(response.status.message)

        emit(DataState.success(response = null, data = true))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}