package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.market.Market
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MarketUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
) {
    fun execute(): Flow<DataState<List<Market>>> = flow {
        Timber.d("Market Use Case")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenns: ${authToken?.token}")
        val avatars = service.getAvatars(
            token = "Bearer ${authToken?.token}"
        ).toMarket()
        Timber.d("Profil Use Case success: ${avatars?.get(0)?.id}")
        emit(DataState.success(response = null, data = avatars))
    }.catch {
        Timber.e("Profile Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}
