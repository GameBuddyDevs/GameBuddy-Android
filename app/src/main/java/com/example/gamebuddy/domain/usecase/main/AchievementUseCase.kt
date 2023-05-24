package com.example.gamebuddy.domain.usecase.main

import com.example.gamebuddy.data.local.auth.AuthTokenDao
import com.example.gamebuddy.data.local.auth.toAuthToken
import com.example.gamebuddy.data.remote.network.GameBuddyApiAppService
import com.example.gamebuddy.domain.model.achievement.Achievement
import com.example.gamebuddy.util.DataState
import com.example.gamebuddy.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class AchievementUseCase(
    private val service: GameBuddyApiAppService,
    private val authTokenDao: AuthTokenDao
) {
    fun execute(): Flow<DataState<List<Achievement>>> = flow {
        Timber.d("Achievement Use Case")
        emit(DataState.loading())
        val authToken = authTokenDao.getAuthToken()?.toAuthToken()
        Timber.d("Tokenns: ${authToken?.token}")
        val achievement = service.getAchievements(
            token = "Bearer ${authToken?.token}"
        ).toAchievement()
        Timber.d("Achievement Use Case success: ${achievement?.get(0)?.id}")
        emit(DataState.success(response = null, data = achievement))
    }.catch {
        Timber.e("Achievement Use Case Error $it")
        emit(handleUseCaseException(it))
    }
}