package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CoinUsecase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
) {
    fun execute(): Flow<DataState<Int>> = flow{
        Timber.d("Coin Use Case")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenss: ${authToken?.token}")
        val coin = service.getProfile(
            token = "Bearer ${authToken?.token}",
            userId = authToken!!.pk
        ).toCoin()
        Timber.d("Coin Use Case success: $coin")
        emit(DataState.success(response = null, data = coin))
    }.catch {
        Timber.e("Profile Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}