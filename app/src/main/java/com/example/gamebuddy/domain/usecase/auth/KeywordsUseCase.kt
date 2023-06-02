package com.example.gamebuddy.domain.usecase.auth

import com.example.gamebuddy.data.remote.model.keyword.KeywordResponse
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.data.remote.network.GameBuddyApiAuthService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class KeywordsUseCase(
    private val service: GameBuddyApiAppService,
) {
    fun execute(): Flow<DataState<KeywordResponse>> = flow {
        emit(DataState.loading())

        val keywords = service.getKeywords()

        if (!keywords.status.success) {
            throw Exception(keywords.status.message)
        }


        emit(DataState.success(response = null, data = keywords))
    }.catch {
        emit(handleUseCaseException(it))
    }
}