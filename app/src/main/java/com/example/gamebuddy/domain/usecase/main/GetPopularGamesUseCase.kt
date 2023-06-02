package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.popular.PopularGames
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetPopularGamesUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
) {
    fun execute(): Flow<DataState<List<PopularGames>>> = flow {
        emit(DataState.loading())

        val authToken = authTokenDao.getAuthToken()?.toAuthToken()

        if (authToken == null) {
            Timber.d("Auth token is null")
            throw Exception("Please login again.")
        }

        val popular = service.getPopularGames(
            token = "Bearer ${authToken.token}",
        ).toPopularGames()

        emit(DataState.success(response = null, data = popular))
    }.catch {
        Timber.e("GetPopularUseCase Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}