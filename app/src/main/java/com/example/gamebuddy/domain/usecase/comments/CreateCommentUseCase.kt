package com.example.gamebuddy.domain.usecase.comments

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.comment.Comment
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.data.remote.request.CreateCommentRequest
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CreateCommentUseCase(
    private val service: GameBuddyApiCommunityService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(
        postId: String,
        comment: String
    ): Flow<DataState<Comment>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()?.token ?: ""

        if (authToken.isEmpty()) {
            Timber.d("Auth token is empty")
            throw Exception("Please login again.")
        }

        val response = service.createComment(
            token = "Bearer $authToken",
            comment = CreateCommentRequest(
                postId = postId,
                message = comment
            )
        )

        if (!response.status.success) {
            Timber.d("Response is not successful")
            throw Exception("Something went wrong.")
        }

        emit(
            DataState.success(
                response = null, data = Comment(
                    avatar = "",
                    commentId = "",
                    likeCount = 0,
                    message = comment,
                    updatedDate = "",
                    username = ""
                )
            )
        )
    }.catch { e ->
        Timber.e("CreateCommentUseCase Error: ${e.printStackTrace()}")
        emit(handleUseCaseException(e))
    }
}