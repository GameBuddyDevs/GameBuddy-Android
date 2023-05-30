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

class BuyItemUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao,
)  {
    fun execute(
        avatarId: String?
    ): Flow<DataState<Boolean>> = flow{
        Timber.d("BuyItem Use Case")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenss: ${authToken?.token}")
        val response = service.buyItem(
            token = "Bearer ${authToken?.token}",
            itemId = avatarId!!
        )
        if (!response.status.success) {
            Timber.e("BuyItemUseCase ${response.status.message}")
            throw Exception(response.status.message)
        }
        emit(DataState.success(response = null, data = true))
    }.catch { e->
        Timber.e("BuyItem ERROR: $e")
        emit(handleUseCaseException(e))
    }
}