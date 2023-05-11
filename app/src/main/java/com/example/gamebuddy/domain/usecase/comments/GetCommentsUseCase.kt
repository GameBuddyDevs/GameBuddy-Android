package com.example.gamebuddy.domain.usecase.comments

import com.example.gamebuddy.data.remote.model.comment.Comment
import com.example.gamebuddy.data.remote.model.comment.CommentData
import com.example.gamebuddy.data.remote.network.GameBuddyApiCommunityService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetCommentsUseCase(
    private val service: GameBuddyApiCommunityService
) {

    fun execute(postId: String): Flow<DataState<List<Comment>>> = flow {
        emit(DataState.loading())

        val response = service.getComments(postId)

        if (!response.status.success)
            throw Exception(response.status.message)

        emit(DataState.success(response = null, data = response.body.data.comments))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}