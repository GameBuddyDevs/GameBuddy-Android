package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.remote.model.gamedetail.GameData
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetGameDetailUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
) {

    fun execute(
        gameId: String
    ): Flow<DataState<GameData>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()?.token ?: ""

        if (authToken.isEmpty()) {
            Timber.d("Auth token is empty")
            throw Exception("Please login again.")
        }

        val response = service.getGameDetail(
            token = "Bearer $authToken",
            gameId = gameId
        )

        if (!response.status.success) {
            throw Exception("Error getting game detail")
        }

        emit(DataState.success(response = null, data = response.body.data.gameData))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}