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


class GetPopularUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
) {
    fun execute(): Flow<DataState<List<PopularGames>>> = flow{
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenss: ${authToken?.token}")
        val popular = service.getPopularGames(
            token = "Bearer ${authToken?.token}",
        ).toPopularGames()
        if (popular.isNotEmpty()){
            Timber.d("GetPopular Use Case success: ${popular[0].gameName}")
        }
        emit(DataState.success(response = null, data = popular))
    }.catch {
        Timber.e("GetPopularUseCase Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}